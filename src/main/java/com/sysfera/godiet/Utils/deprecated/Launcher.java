/*@GODIET_LICENSE*/
/*
 * Launcher.java
 *
 * Created on April 19, 2004, 1:59 PM
 */

package com.sysfera.godiet.Utils.deprecated;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.Model.deprecated.AccessMethod;
import com.sysfera.godiet.Model.deprecated.Domain;
import com.sysfera.godiet.Model.deprecated.Elements;
import com.sysfera.godiet.Model.deprecated.RunConfig;
import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;
import com.sysfera.godiet.Model.physicalresources.deprecated.StorageResource;
import com.sysfera.godiet.exceptions.LaunchException;

/**
 * 
 * @author hdail
 */
public class Launcher {
	private ConsoleController consoleCtrl;
	private File killPlatformFile;

	private Set<Domain> domains;

	/** Creates a new instance of Launcher */
	public Launcher(ConsoleController consoleController, Set<Domain> domains) {
		this.consoleCtrl = consoleController;
		this.domains = domains;
	}

	public void createLocalScratch() {
		RunConfig runCfg = consoleCtrl.getRunConfig();
		File dirHdl;
		String runLabel = null;

		SimpleDateFormat formatter = new SimpleDateFormat("yyMMMdd_HHmm");
		java.util.Date today = new Date();
		String dateString = formatter.format(today);

		for (Domain domain : domains) {
			dirHdl = new File(runCfg.getLocalScratch() + "/" + domain.getName());
			dirHdl.mkdirs();

			runCfg.setRunLabel(null);

		}

		runCfg.setLocalScratchReady(true);
		consoleCtrl.printOutput(
				"\nLocal scratch directory ready:\n\t"
						+ runCfg.getLocalScratch(), 1);

		// Initiate output file for pids to use as backup for failures
		killPlatformFile = new File(runCfg.getLocalScratch(),
				"killPlatform.csh");
		try {
			if (killPlatformFile.exists()) {
				killPlatformFile.delete();
			}
			killPlatformFile.createNewFile();
		} catch (IOException x) {
			consoleCtrl.printOutput("createLocalScratch: Could not create "
					+ killPlatformFile);
		}
	}


	/*
	 * launchElement is the second method for launching components of the DIET
	 * hierarchy. This method performs the following actions: - check that
	 * element, compRes, & scratch base are non-null - run the element on the
	 * remote host - don't need to create and stage cfg file, it suppose to be
	 * already done.
	 */
	public void launchElement(Elements element, boolean useLogService) {
		if (element == null) {
			consoleCtrl.printError("launchElement called with null element. "
					+ "Launch request ignored.", 1);
			return;
		}
		if (element.getComputeResource() == null) {
			consoleCtrl.printError("LaunchElement called with null resource. "
					+ "Launch request ignored.");
			return;
		}
		consoleCtrl.printOutput("\n** Launching element " + element.getName()
				+ " on " + element.getComputeResource().getName(), 1);
		// LAUNCH STAGE 1: Launch element
		runElement(element);
	}

	public void stageFile(String filename, StorageResource storeRes)
			throws LaunchException {
		consoleCtrl.printOutput(
				"Staging file " + filename + " to " + storeRes.getName(), 1);

		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);
		sshUtil.stageWithScp(filename, storeRes, consoleCtrl.getRunConfig());
	}

	// TODO: incorporate Elagi usage
	public void stageAllFile(StorageResource storeRes) throws LaunchException {
		consoleCtrl.printOutput("Staging file to " + storeRes.getName(), 1);

		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);
		sshUtil.stageFilesWithScp(storeRes, consoleCtrl.getRunConfig());
	}

	// TODO: incorporate Elagi usage
	private void runElement(Elements element) {
		ComputeResource compRes = element.getComputeResource();
		consoleCtrl.printOutput("Executing element " + element.getName()
				+ " on resource " + compRes.getName(), 1);
		AccessMethod access = compRes.getAccessMethod("ssh");
		if (access == null) {
			consoleCtrl.printError("runElement: compRes does not have "
					+ "ssh access method. Ignoring launch request.");
			return;
		}

		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);
		sshUtil.runWithSsh(element, consoleCtrl.getRunConfig(),
				killPlatformFile);
	}

	public void stopElement(Elements element) {
		consoleCtrl.printOutput("Trying to stop element " + element.getName(),
				1);
		SshUtils sshUtil = new SshUtilsImpl(consoleCtrl);

		sshUtil.stopWithSsh(element, consoleCtrl.getRunConfig());

	}

	public void createCfgFile(Elements element, boolean useLogService)
			throws IOException {
		RunConfig runCfg = consoleCtrl.getRunConfig();
		if (element != null) {
			if (element.getName().compareTo("TestTool") == 0) {
				return;
			}
			File cfgFile = new File(runCfg.getLocalScratch() + "/" +

			element.getCfgFileName());
			try {
				cfgFile.createNewFile();
				consoleCtrl.printOutput(
						"Writing config file " + element.getCfgFileName(), 1);
				FileWriter out = new FileWriter(cfgFile);
				element.writeCfgFile(out);
				out.close();
			} catch (IOException x) {
				consoleCtrl.printError("Failed to write " + cfgFile.getPath());
				throw x;
			}
		}
	}

}