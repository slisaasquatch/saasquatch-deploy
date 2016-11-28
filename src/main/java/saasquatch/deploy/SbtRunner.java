package saasquatch.deploy;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class SbtRunner {
	
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	private final Properties prop;
	private final File appDir;
	private final String sbtExecutable;

	public SbtRunner(Properties prop) {
		this.prop = prop;
		this.appDir = new File(this.prop.getProperty(Constants.Keys.APP_DIR));
		this.sbtExecutable = this.prop.getProperty(Constants.Keys.SBT_EXEC_PATH);
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
		System.out.println("Done sbt dist!");
	}

}
