package saasquatch.deploy;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.configuration2.Configuration;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public class SquatchZip {
	
	private final Configuration config;
	private final String originalZipName;
	private final String targetProjectDir;
	
	public SquatchZip(Configuration config) {
		this.config = config;
		this.originalZipName = config.getString(Constants.Keys.TARGET_ZIP_PATH);
		this.targetProjectDir = config.getString(Constants.Keys.TARGET_PROJECT_DIR);
	}
	
	public void extractOriginalZip() {
		extractToSameDir(originalZipName);
	}
	
	public void compressNewZip() {
		createZipFromDir(targetProjectDir, getNewZipName());
	}
	
	public String getNewZipName() {
//		RevCommit latestCommit = new JgitUtils(config).getLatestCommitInCurrentBranch();
//		String shortSha1 = JgitUtils.getShortSha1FromRevCommit(latestCommit);
//		String newZipName = "saasquatch-"
//				+ AppEnvironment.getCurrent(config).toString().toLowerCase()
//				+ "-" + shortSha1;
//		return newZipName;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return "saasquatch-"
				+ AppEnvironment.getCurrent(config).toString().toLowerCase()
				+ "-" + sdf.format(new Date()) + ".zip";
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
	
	public static void createZipFromDir(String dir, String zipName) {
		File dirFile = new File(dir);
		if (!dirFile.isDirectory()) return;
		System.out.println("Creating new zip: " + zipName);
		try {
			ZipFile zipFile = new ZipFile(dirFile.getParentFile().getAbsolutePath()
					+ File.separator + zipName);
			ZipParameters params = new ZipParameters();
			params.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
			params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_ULTRA);
			zipFile.addFolder(dir, params);
		} catch (ZipException e) {
			e.printStackTrace();
		}
		System.out.println("Done creating zip!");
	}
	
}
