package saasquatch.deploy;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.configuration2.Configuration;

import com.amazonaws.auth.AWSCredentials;
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
	
	private final AmazonS3 s3Client;

	public S3Uploader(Configuration config) {
		this.s3Client = new AmazonS3Client(getAWSCredentials());
	}
	
	public void upload(File file, String existingBucketName, String keyName) {
		List<PartETag> partETags = new ArrayList<PartETag>();
		
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(
				existingBucketName, keyName);
		InitiateMultipartUploadResult initResponse =
				s3Client.initiateMultipartUpload(initRequest);
		
		long contentLength = file.length();
		long partSize = 5 * 1024 * 1024;
		try {
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
		        
		        partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());
		        filePosition += partSize;
		        
		        CompleteMultipartUploadRequest compRequest =
		        		new CompleteMultipartUploadRequest(existingBucketName,
		        				keyName, initResponse.getUploadId(), partETags);
		        
		        s3Client.completeMultipartUpload(compRequest);
			}
		} catch (Exception e) {
			s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
					existingBucketName, keyName, initResponse.getUploadId()));
		}
	}
	
	private static AWSCredentials getAWSCredentials() {
		String key = System.getenv(Constants.EnvKeys.WEB_AWS_ACCESS_KEY);
		String secret = System.getenv(Constants.EnvKeys.WEB_AWS_SECRET);
		return new BasicAWSCredentials(key, secret);
	}

}
