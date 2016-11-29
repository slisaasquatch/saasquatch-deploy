package saasquatch.deploy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;

public class DeployCleanup {
	
	private final boolean cleanup;
	private final File targetDir;
	
	public DeployCleanup(Configuration config) {
		this.cleanup = config.getBoolean(Constants.Keys.TARGET_DIR_CLEANUP);
		this.targetDir = new File(config.getString(Constants.Keys.TARGET_DIR));
	}
	
	public void doCleanup() {
		if (cleanup) {
			System.out.println("Starting cleanup...");
			try {
				FileUtils.deleteDirectory(targetDir);
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Done cleanup!");
		}
	}

}
