package saasquatch.deploy;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class Unzip {
	
	public static void extractToSameDir(String zipName) {
		File originalZipFile = new File(zipName);
		System.out.println("Unzipping " + originalZipFile.getAbsolutePath());
		try {
			ZipFile zipFile = new ZipFile(originalZipFile);
			zipFile.extractAll(originalZipFile.getParentFile().getAbsolutePath());
		} catch (ZipException e) {
			e.printStackTrace();
		}
		System.out.println("Done unzipping!");
	}
	
}
