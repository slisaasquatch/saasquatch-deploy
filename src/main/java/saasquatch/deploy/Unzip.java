package saasquatch.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.sun.istack.internal.Nullable;

public class Unzip {
	
	private static void unzip(String zipFile, @Nullable String outputFolder){

		byte[] buffer = new byte[1024];

		try {
			if (outputFolder != null) {
				//create output directory is not exists
				File folder = new File(outputFolder);
				if(!folder.exists()) {
					folder.mkdir();
				}
			}

	    	//get the zip file content
	    	ZipInputStream zis =
	    		new ZipInputStream(new FileInputStream(zipFile));
	    	//get the zipped file list entry
	    	ZipEntry ze = zis.getNextEntry();

	    	while(ze!=null){

	    	   String fileName = ze.getName();
	    	   if (outputFolder != null) {
	    		   fileName = outputFolder + File.separator + fileName;
	    	   }
	           File newFile = new File(fileName);

	           System.out.println("file unzip : "+ newFile.getAbsoluteFile());

	            //create all non exists folders
	            //else you will hit FileNotFoundException for compressed folder
	            new File(newFile.getParent()).mkdirs();

	            FileOutputStream fos = new FileOutputStream(newFile);

	            int len;
	            while ((len = zis.read(buffer)) > 0) {
	       		fos.write(buffer, 0, len);
	            }

	            fos.close();
	            ze = zis.getNextEntry();
	    	}

	        zis.closeEntry();
	    	zis.close();

	    } catch (IOException ex) {
	    	ex.printStackTrace();
	    }
	}

}
