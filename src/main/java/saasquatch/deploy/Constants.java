package saasquatch.deploy;

public class Constants {
	
	public static final String DEFAULT_PROP_NAME = "config.properties";
	
	public static class Keys {
		
		public static final String
				SBT_EXEC_PATH = "sbt.exec.path",
				APP_ENVIRONMENT = "app.environment",
				PROJECT_DIR = "project.dir",
				PROJECT_GIT_DIR = "project.git.dir",
				PROJECT_ASSETS_DIR = "project.assets.dir",
				TARGET_DIR = "target.dir",
				TARGET_ZIP_PATH = "target.zip.path",
				TARGET_PROJECT_DIR = "target.project.dir",
				TARGET_PROJECT_ASSETS_DIR = "target.project.assets.dir",
				S3_BUCKET_NAME = "s3.bucket.name";
		
	}
	
	public static class EnvKeys {
		
		public static final String
				WEB_AWS_ACCESS_KEY = "WEB_AWS_ACCESS_KEY",
				WEB_AWS_SECRET = "WEB_AWS_SECRET";
		
	}

}
