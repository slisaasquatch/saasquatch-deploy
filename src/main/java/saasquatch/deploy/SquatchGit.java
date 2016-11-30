package saasquatch.deploy;

import static org.eclipse.jgit.lib.Constants.HEAD;
import static org.eclipse.jgit.lib.Constants.R_HEADS;
import static saasquatch.deploy.Main.err;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.configuration2.Configuration;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;

public class SquatchGit {
	
	private final Git git;
	private final boolean allowUncommittedChanges;

	public SquatchGit(Configuration config) {
		try {
			git = Git.open(new File(config.getString(Constants.Config.PROJECT_GIT_DIR)));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		this.allowUncommittedChanges = config.getBoolean(
				Constants.Config.ALLOW_UNCOMMITTED_CHANGES);
	}
	
	public Git getGit() {
		return git;
	}
	
	public void checkUncommittedChanges() {
		if (allowUncommittedChanges) {
			System.out.println(Constants.Config.ALLOW_UNCOMMITTED_CHANGES
					+ " is set to true. Ignoring uncommitted changes if there are any.");
		} else {
			System.out.println("Checking uncommitted changes...");
			boolean ok = false;
			try {
				ok = !git.status().call().hasUncommittedChanges();
			} catch (NoWorkTreeException | GitAPIException e) {
				e.printStackTrace();
			}
			if (ok) {
				System.out.println("Done! No uncommitted changes found.");
			} else {
				err("There are uncommitted changes in the repository. Deployment aborted.");
			}
		}
	}
	
	public String getLatestCommitShortName() {
		return getLatestCommitName().substring(0, 7);
	}
	
	public String getLatestCommitName() {
		try {
			return git.getRepository().findRef(HEAD).getObjectId().name();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public RevCommit getLatestCommitInBranch(String branchName) {
		List<RevCommit> commits = getCommitsInBranch(branchName);
		if (commits.size() < 1) {
			return null;
		}
		commits.sort((c1, c2) -> {
			return c1.getAuthorIdent().getWhen().compareTo(c2.getAuthorIdent().getWhen());
		});
		return commits.get(0);
	}
	
	public List<RevCommit> getCommitsInBranch(String branchName) {
		Repository repo = git.getRepository();
		RevWalk walk = new RevWalk(repo);
		List<RevCommit> commitsFound = Collections.synchronizedList(new ArrayList<>());
		List<Ref> branches = null;
		try {
			branches = git.branchList().call();
		} catch (GitAPIException e) {
			e.printStackTrace();
		}
		branches.stream().parallel()
				.filter(branch -> branch.getName().equals(branchName)
						|| branch.getName().endsWith(branchName))
				.forEach(branch -> {
					Iterable<RevCommit> commits = null; 
					try {
						commits = git.log().all().call();
					} catch (GitAPIException | IOException e) {
						e.printStackTrace();
					}
					for (RevCommit commit : commits) {
						boolean foundInThisBranch = false;
						RevCommit targetCommit = null;
						try {
							targetCommit = walk.parseCommit(repo.resolve(commit.name()));
						} catch (RevisionSyntaxException | IOException e) {
							e.printStackTrace();
						}
						for (Map.Entry<String, Ref> e : repo.getAllRefs().entrySet()) {
			                if (e.getKey().startsWith(R_HEADS)) {
			                    try {
									if (walk.isMergedInto(targetCommit, walk.parseCommit(
									        e.getValue().getObjectId()))) {
									    String foundInBranch = e.getValue().getName();
									    if (branchName.equals(foundInBranch)) {
									        foundInThisBranch = true;
									        break;
									    }
									}
								} catch (IOException e1) {
									e1.printStackTrace();
								}
			                }
			            }
						if (foundInThisBranch) {
							commitsFound.add(commit);
						}
					}
				});
		walk.close();
		return commitsFound;
	}
	
	public static String getShortSha1FromRevCommit(RevCommit commit) {
		return commit.name().substring(0, 7);
	}

}
