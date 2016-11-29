package saasquatch.deploy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;

public class DeployCleanup {
	
	private final String cleanup;
	private final String targetDir;
	
	public DeployCleanup(Configuration config) {
		this.cleanup = config.getString(Constants.Keys.TARGET_DIR_CLEANUP);
		this.targetDir = config.getString(Constants.Keys.TARGET_DIR);
	}
	
	public void doCleanup() {
		if (Boolean.parseBoolean(cleanup.trim().toLowerCase())) {
			System.out.println("Starting cleanup...");
			try {
				FileUtils.deleteDirectory(new File(targetDir));
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Done cleanup!");
		}
	}

}
