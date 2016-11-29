package saasquatch.deploy;

import java.io.File;
import java.io.IOException;

import org.apache.commons.configuration2.Configuration;

public class SbtRunner {
	
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	private final File appDir;
	private final String sbtExecutable;

	public SbtRunner(Configuration config) {
		this.appDir = new File(config.getString(Constants.Keys.PROJECT_DIR));
		this.sbtExecutable = config.getString(Constants.Keys.SBT_EXEC_PATH);
	}
	
	public void dist() {
		System.out.println("Running sbt dist...");
		Process p;
		try {
			p = RUNTIME.exec(new String[] {sbtExecutable, "dist"}, null, appDir);
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Done running sbt dist!");
	}

}
