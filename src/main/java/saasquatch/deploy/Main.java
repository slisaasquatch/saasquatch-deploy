package saasquatch.deploy;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.configuration2.Configuration;

public class Main {
	
	public static void main(String[] args) {
		Instant startTime = Instant.now();
		Configuration config = new ConfigGetter(args).getConfiguration();
		
		// Run sbt dist
		new SbtRunner(config).dist();
		
		// Extract saasquatch zip
		new SquatchZip(config).extractOriginalZip();
		
		// Copy over app/assets directory
		new AssetsCopier(config).copy();
		
		// Re-zip
		new SquatchZip(config).compressNewZip();
		
		// Upload to S3
		
		// Cleanup
		
		System.out.println("Done!");
		Instant endTime = Instant.now();
		System.out.println("Elapsed time: "
				+ Duration.between(startTime, endTime).getSeconds() + " seconds");
	}

}