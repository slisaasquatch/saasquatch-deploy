package saasquatch.deploy;

import org.apache.commons.configuration2.Configuration;

public enum AppEnvironment {
	
	PRODUCTION,
	STAGING;
	
//	public abstract String getBranchName(Configuration config);
	
	@Override
	public String toString() {
		return name();
	}
	
	public boolean is(String name) {
		if (name == null) return false;
		return name.trim().equalsIgnoreCase(toString());
	}
	
	public static AppEnvironment fromString(String name) {
		for (AppEnvironment env : values()) {
			if (env.is(name)) {
				return env;
			}
		}
		return null;
	}
	
	public static AppEnvironment getCurrent(Configuration config) {
		return fromString(config.getString(Constants.Config.APP_ENVIRONMENT));
	}

}
