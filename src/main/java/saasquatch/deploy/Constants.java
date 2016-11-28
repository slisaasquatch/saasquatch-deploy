package saasquatch.deploy;

public class Constants {
	
	public static final String DEFAULT_PROP_NAME = "config.properties";
	
	public static class Keys {
		
		public static final String
				SBT_EXEC_PATH = "sbt.exec.path",
				APP_ENVIRONMENT = "app.environment",
				PRODUCTION_BRANCH = "production.branch",
				STAGING_BRANCH = "staging.branch",
				PROJECT_DIR = "project.dir",
				PROJECT_GIT_DIR = "project.git.dir",
				PROJECT_ASSETS_DIR = "project.assets.dir",
				TARGET_DIR = "target.dir",
				TARGET_ZIP_PATH = "target.zip.path",
				TARGET_PROJECT_DIR = "target.project.dir",
				TARGET_PROJECT_ASSETS_DIR = "target.project.assets.dir";
		
	}

}
