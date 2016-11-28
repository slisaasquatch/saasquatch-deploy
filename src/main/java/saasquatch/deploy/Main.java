package saasquatch.deploy;

import java.time.Duration;
import java.time.Instant;

import org.apache.commons.configuration2.Configuration;

public class Main {
	
	public static void main(String[] args) {
		Instant startTime = Instant.now();
		Configuration config = new ConfigGetter(args).getConfiguration();
		new SbtRunner(config).dist();
		//Unzip.unzip(prop.getProperty(Constants.Keys.TARGET_DIR) + "/saasquatch-1.3.0.zip", null);
		Unzip.extractToSameDir(config.getString(Constants.Keys.TARGET_ZIP_PATH));
		
		System.out.println("Done!");
		Instant endTime = Instant.now();
		System.out.println("Elapsed time: "
				+ Duration.between(startTime, endTime).getSeconds() + " seconds");
	}

}
