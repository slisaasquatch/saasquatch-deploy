package saasquatch.deploy;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.configuration2.Configuration;

public class Main {
	
	public static void main(String[] args) {
		Instant startTime = Instant.now();
		Configuration config = new ConfigGetter(args).getConfiguration();
		new SbtRunner(config).dist();
		ZipUtils.extractToSameDir(config.getString(Constants.Keys.TARGET_ZIP_PATH));
		new AssetsCopier(config).copy();
		
		System.out.println("Done!");
		Instant endTime = Instant.now();
		System.out.println("Elapsed time: "
				+ Duration.between(startTime, endTime).getSeconds() + " seconds");
	}

}
