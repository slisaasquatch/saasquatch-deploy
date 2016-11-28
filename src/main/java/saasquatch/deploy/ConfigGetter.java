package saasquatch.deploy;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

public class ConfigGetter {
	
	private final String[] args;
	private final String propName;
	
	public ConfigGetter(String[] args) {
		this.args = args.clone();
		
		if (this.args.length == 0) {
			this.propName = Constants.DEFAULT_PROP_NAME;
		} else {
			this.propName = args[0];
		}
		
	}
	
	public Configuration getConfiguration() {
		Parameters params = new Parameters();
		FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
				new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
				.configure(params.properties().setFileName(propName));
		try {
			return builder.getConfiguration();
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Properties getProperties() {
		Properties prop = new Properties();
		try (InputStream input = new FileInputStream(propName)) {
			prop.load(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}

}
