package saasquatch.deploy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;

public class AssetsCopier {
	
	private final String originalAssetsDir;
	private final String targetAssetsDir;
	
	public AssetsCopier(Configuration config) {
		this.originalAssetsDir = config.getString(Constants.Keys.PROJECT_ASSETS_DIR);
		this.targetAssetsDir = config.getString(Constants.Keys.TARGET_PROJECT_ASSETS_DIR);
	}
	
	public void copy() {
		System.out.println("Copying assets directory...");
		try {
			FileUtils.copyDirectory(new File(originalAssetsDir), new File(targetAssetsDir));
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done copying assets directory!");
	}

}
