package saasquatch.deploy;

import java.time.Duration;
import java.time.Instant;
import java.util.Properties;

import org.apache.commons.configuration2.Configuration;

public class Main {
	
	public static void main(String[] args) {
		Instant startTime = Instant.now();
		Properties prop = new ConfigGetter(args).getProperties();
		Configuration config = new ConfigGetter(args).getConfiguration();
		//new SbtRunner(prop).dist();
		//Unzip.unzip(prop.getProperty(Constants.Keys.TARGET_DIR) + "/saasquatch-1.3.0.zip", null);
		
		System.out.println("Done!");
		Instant endTime = Instant.now();
		System.out.println("Elapsed time: "
				+ Duration.between(startTime, endTime).getSeconds() + " seconds");
	}

}
