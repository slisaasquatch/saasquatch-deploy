package saasquatch.deploy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.io.FileUtils;

public class AssetsCopier {
	
	private final File originalAssetsDir;
	private final File targetAssetsDir;
	
	public AssetsCopier(Configuration config) {
		this.originalAssetsDir = new File(config.getString(
				Constants.Config.PROJECT_ASSETS_DIR));
		this.targetAssetsDir = new File(config.getString(
				Constants.Config.TARGET_PROJECT_ASSETS_DIR));
	}
	
	public void copy() {
		System.out.println("Copying assets directory...");
		try {
			FileUtils.copyDirectory(originalAssetsDir, targetAssetsDir);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Done copying assets directory!");
	}

}
