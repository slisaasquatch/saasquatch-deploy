package saasquatch.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.lang3.StringUtils;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class S3Uploader {
	
	private final Configuration config;
	private final boolean doUpload;
	private final AmazonS3 s3Client;
	private final String bucketName;

	public S3Uploader(Configuration config) {
		this.config = config;
		this.doUpload = config.getBoolean(Constants.Config.S3_DO_UPLOAD);
		this.s3Client = new AmazonS3Client(getBasicAWSCredentials());
		this.bucketName = config.getString(Constants.Config.S3_BUCKET_NAME);
	}
	
	public void upload(File file) {
		if (doUpload) {
			String keyName = AppEnvironment.getCurrent(config).toString().toLowerCase()
					+ "/" + file.getName();
			upload(file, bucketName, keyName);
		} else {
			System.out.println(Constants.Config.S3_DO_UPLOAD
					+ " is set to false. Skipping upload.");
		}
	}
	
	private void upload(File file, String existingBucketName, String keyName) {
		System.out.println("Start uploading " + file.getName() + " ...");
		// Create a list of UploadPartResponse objects. You get one of these
		// for each part upload.
		List<PartETag> partETags = new ArrayList<PartETag>();
		
		// Step 1: Initialize.
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(
				existingBucketName, keyName);
		InitiateMultipartUploadResult initResponse =
				s3Client.initiateMultipartUpload(initRequest);
		
		long contentLength = file.length();
		long partSize = 5 * 1024 * 1024;

		try {
			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
		        // Last part can be less than 5 MB. Adjust part size.
		    	partSize = Math.min(partSize, (contentLength - filePosition));
		    	
		    	// Create request to upload a part.
		        UploadPartRequest uploadRequest = new UploadPartRequest()
						.withBucketName(existingBucketName).withKey(keyName)
						.withUploadId(initResponse.getUploadId()).withPartNumber(i)
						.withFileOffset(filePosition)
						.withFile(file)
						.withPartSize(partSize);
		        
		        // Upload part and add response to our list.
		        partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());
		        filePosition += partSize;
		        System.out.print("Uploaded "
		        		+ StringUtils.leftPad(String.valueOf(filePosition),
		        				String.valueOf(contentLength).length())
		        		+ "/" + contentLength + " bytes\r");
			}
	        
			// Step 3: Complete.
	        CompleteMultipartUploadRequest compRequest =
	        		new CompleteMultipartUploadRequest(existingBucketName,
	        				keyName, initResponse.getUploadId(), partETags);
	        
	        s3Client.completeMultipartUpload(compRequest);
		} catch (Exception e) {
			s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
					existingBucketName, keyName, initResponse.getUploadId()));
		}
		System.out.println("Done uploading!");
	}
	
	private static BasicAWSCredentials getBasicAWSCredentials() {
		String key = System.getenv(Constants.EnvKeys.WEB_AWS_ACCESS_KEY);
		if (key == null) {
			throw new RuntimeException("The environment variable "
					+ Constants.EnvKeys.WEB_AWS_ACCESS_KEY + " is not set.");
		}
		String secret = System.getenv(Constants.EnvKeys.WEB_AWS_SECRET);
		if (secret == null) {
			throw new RuntimeException("The environment variable "
					+ Constants.EnvKeys.WEB_AWS_SECRET + " is not set.");
		}
		return new BasicAWSCredentials(key, secret);
	}

}
