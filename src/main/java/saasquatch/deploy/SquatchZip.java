package saasquatch.deploy;

import java.io.File;

import org.apache.commons.configuration2.Configuration;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class SquatchZip {
	
	private final Configuration config;
	private final String originalZipName;
	private final String targetProjectDirName;
	
	public SquatchZip(Configuration config) {
		this.config = config;
		this.originalZipName = config.getString(Constants.Config.TARGET_ZIP_PATH);
		this.targetProjectDirName = config.getString(Constants.Config.TARGET_PROJECT_DIR);
	}
	
	public void extractOriginalZip() {
		extractToSameDir(originalZipName);
	}
	
	public File compressNewZip() {
		return createZipFromDir(targetProjectDirName, getNewZipName());
	}
	
	public String getNewZipName() {
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		return "saasquatch-" + sdf.format(new Date()) + ".zip";
		return "saasquatch-" + new JgitUtils(config).getLatestCommitShortName() + ".zip";
	}
	
	public static void extractToSameDir(String zipName) {
		File originalZipFile = new File(zipName);
		System.out.println("Unzipping " + originalZipFile.getAbsolutePath() + " ...");
		try {
			ZipFile zipFile = new ZipFile(originalZipFile);
			zipFile.extractAll(originalZipFile.getParentFile().getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}
		System.out.println("Done unzipping!");
	}
	
	public static File createZipFromDir(String dir, String zipName) {
		File dirFile = new File(dir);
		if (!dirFile.isDirectory()) return null;
		System.out.println("Creating new zip: " + zipName + " ...");
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(new File(dirFile.getParentFile(), zipName));
			if (zipFile.getFile().exists()) {
				System.out.println("File: " + zipFile.getFile().getName()
						+ " already exists. It will be overwritten.");
				zipFile.getFile().delete();
			}
			ZipParameters params = new ZipParameters();
			params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
			zipFile.addFolder(dir, params);
		} catch (ZipException e) {
			e.printStackTrace();
		}
		System.out.println("Done creating zip!");
		return zipFile.getFile();
	}
	
}
