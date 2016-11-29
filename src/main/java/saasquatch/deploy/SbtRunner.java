package saasquatch.deploy;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.configuration2.Configuration;

public class SbtRunner {
	
	private static final Runtime RUNTIME = Runtime.getRuntime();
	
	private final boolean printOutput;
	private final File projectDir;
	private final String sbtExecutable;

	public SbtRunner(Configuration config) {
		this.printOutput = config.getBoolean(Constants.Config.SBT_PRINT_OUTPUT);
		this.projectDir = new File(config.getString(Constants.Config.PROJECT_DIR));
		this.sbtExecutable = config.getString(Constants.Config.SBT_EXEC_PATH);
	}
	
	public void dist() {
		System.out.println("Running sbt dist...");
		try {
			Process p = RUNTIME.exec(new String[] {sbtExecutable, "dist"}, null, projectDir);
			if (printOutput) {
				CompletableFuture.runAsync(() -> {
					try (Scanner scanner = new Scanner(p.getInputStream())) {
						while (scanner.hasNextLine()) {
							System.out.println(scanner.nextLine());
						}
					}
				});
				CompletableFuture.runAsync(() -> {
					try (Scanner scanner = new Scanner(p.getErrorStream())) {
						while (scanner.hasNextLine()) {
							System.err.println(scanner.nextLine());
						}
					}
				});
			}
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Done running sbt dist!");
	}

}
