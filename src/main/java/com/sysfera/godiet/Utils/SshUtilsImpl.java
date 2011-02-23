/*@GODIET_LICENSE*/
/*
 * SshUtils.java
 *
 * Created on May 10, 2004, 1:59 PM
 */

package com.sysfera.godiet.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.sysfera.godiet.Controller.ConsoleController;
import com.sysfera.godiet.Model.deprecated.AccessMethod;
import com.sysfera.godiet.Model.deprecated.Elements;
import com.sysfera.godiet.Model.deprecated.EnvVar;
import com.sysfera.godiet.Model.deprecated.Forwarder;
import com.sysfera.godiet.Model.deprecated.LaunchInfo;
import com.sysfera.godiet.Model.deprecated.LocalAgent;
import com.sysfera.godiet.Model.deprecated.Ma_dag;
import com.sysfera.godiet.Model.deprecated.MasterAgent;
import com.sysfera.godiet.Model.deprecated.OmniNames;
import com.sysfera.godiet.Model.deprecated.RunConfig;
import com.sysfera.godiet.Model.deprecated.ServerDaemon;
import com.sysfera.godiet.Model.physicalresources.deprecated.ComputeResource;
import com.sysfera.godiet.Model.physicalresources.deprecated.StorageResource;
import com.sysfera.godiet.exceptions.LaunchException;

/**
 * 
 * @author hdail
 */
public class SshUtilsImpl implements SshUtils {
	private ConsoleController consoleCtrl;

	/** Creates a new instance of SshUtils */
	public SshUtilsImpl(ConsoleController consoleController) {
		this.consoleCtrl = consoleController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sysfera.godiet.Utils.SshUtils#stageWithScp(java.lang.String,
	 * com.sysfera.godiet.Model.physicalresources.StorageResource,
	 * com.sysfera.godiet.Model.RunConfig)
	 */
	@Override
	public void stageWithScp(String filename, StorageResource storeRes,
			RunConfig runConfig) {
		AccessMethod access = storeRes.getAccessMethod("scp");

		String command = new String("/usr/bin/scp ");

		// TODO: recursive copy won't work with JuxMem ... find other way
		// to create remote storage without recursion
		// If remote scratch not yet available, create it and stage file by
		// recursive copy. Else, copy just the file.

		// scp localScratchBase/* remoteScratchBase/
		// if (!filename.equals("omniORB4.cfg"))
		// command += runConfig.getLocalScratch() + "/omniORB4.cfg "; //
		// omniORB

		command += runConfig.getLocalScratch() + "/" + filename + " "; // source

		command += access.getLogin() + "@" + access.getServer() + ":";
		command += storeRes.getScratchBase();

		java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
		consoleCtrl.printOutput("Running: " + command, 2);
		try {
			runtime.exec(command);
		} catch (IOException x) {
			consoleCtrl.printError("stageWithScp failed.", 0);
		}
		if (!storeRes.getScratchReady()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException x) {
				consoleCtrl.printError("stageWithScp: Unexpected sleep "
						+ "interruption. Exiting.", 0);
				System.exit(1);
			}
			storeRes.setScratchReady(true);
		}
		storeRes.setRunLabel(runConfig.getRunLabel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sysfera.godiet.Utils.SshUtils#stageFilesWithScp(com.sysfera.godiet
	 * .Model.physicalresources.StorageResource,
	 * com.sysfera.godiet.Model.RunConfig)
	 */
	@Override
	public void stageFilesWithScp(StorageResource storeRes, RunConfig runConfig)
			throws LaunchException {
		AccessMethod access = storeRes.getAccessMethod("scp");
		String command = new String("/usr/bin/scp ");

		java.lang.Runtime rt = java.lang.Runtime.getRuntime();
		try {
			rt.exec("/usr/bin/ssh " + access.getLogin() + "@"
					+ access.getServer() + " /bin/mkdir -p "
					+ storeRes.getScratchBase() + "/"
					+ storeRes.getDomain().getName() + "/");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new LaunchException("Unable to create direcory", e);
		}
		List<String> files = new ArrayList<String>();
		for (Iterator<Elements> itEl = storeRes.getElementList().iterator(); itEl
				.hasNext();) {
			Elements el = (Elements) itEl.next();
			files.add(el.getCfgFileName());
		}
		if (!files.contains("omniORB4.cfg"))
			files.add("omniORB4.cfg");
		for (Iterator<String> itFile = files.iterator(); itFile.hasNext();) {
			String filename = itFile.next().toString();
			consoleCtrl.printOutput("file : " + filename, 1);
			command += runConfig.getLocalScratch() + "/" + filename + " "; // source
		}
		command += access.getLogin() + "@" + access.getServer() + ":";

		command += storeRes.getScratchBase() + "/"
				+ storeRes.getDomain().getName();

		java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
		// consoleCtrl.printOutput("##############",2);
		consoleCtrl.printOutput("Running: " + command, 2);
		// consoleCtrl.printOutput("##############",2);
		try {
			runtime.exec(command);
		} catch (IOException x) {
			consoleCtrl.printError("stageWithScp failed.", 0);
		}
		if (!storeRes.getScratchReady()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException x) {
				consoleCtrl.printError("stageWithScp: Unexpected sleep "
						+ "interruption. Exiting.", 0);
				System.exit(1);
			}
			storeRes.setScratchReady(true);
		}
		storeRes.setRunLabel(runConfig.getRunLabel());
	}

	// TODO: incorporate Elagi usage
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sysfera.godiet.Utils.SshUtils#runWithSsh(com.sysfera.godiet.Model
	 * .Elements, com.sysfera.godiet.Model.RunConfig, java.io.File)
	 */
	@Override
	public void runWithSsh(Elements element, RunConfig runConfig,
			File killBackup) {
		ComputeResource compRes = element.getComputeResource();
		StorageResource storage = compRes.getCollection().getStorageResource();
		String scratch;
		int i;

		scratch = storage.getScratchBase();

		AccessMethod access = compRes.getAccessMethod("ssh");
		if (access == null) {
			System.err.println("runElement: compRes does not have ssh access "
					+ "method. Ignoring launch request");
			return;
		}

		/**
		 * If element is omniNames, need to ensure old log file is deleted so
		 * can use "omniNames -start port" command. Unless we want to restart
		 * omniNames with the backup file, in this case the "- start" option is
		 * removed, and we do not remove log and backup files.
		 */
		if ((element instanceof OmniNames)
				&& !((OmniNames) element).getBackupRestart()) {
			String omniRemove = "/bin/sh -c \" /bin/rm -f " + scratch
					+ "/omninames*.log ";
			omniRemove += scratch + "/omninames*.bak \" ";

			String[] commandOmni = { "/usr/bin/ssh",
					access.getLogin() + "@" + access.getServer(), omniRemove };

			for (i = 0; i < commandOmni.length; i++) {
				consoleCtrl.printOutput("Command element " + i + " is "
						+ commandOmni[i], 2);
			}
			try {
				Runtime.getRuntime().exec(commandOmni);
			} catch (IOException x) {
				consoleCtrl.printError(
						"runElement failed to remove omni log file.", 0);
				x.printStackTrace();
			}
		}

		/** Build remote command for launching the job */
		String remoteCommand = "";
		if ((element instanceof MasterAgent) || (element instanceof Ma_dag)
				|| (element instanceof LocalAgent)
				|| (element instanceof ServerDaemon)) {
			if (element.getUseDietStats()) {
				remoteCommand += "export DIET_STAT_FILE_NAME=" + scratch + "/"
						+ element.getName() + ".stats ; ";
			}
		}
		// Set OMNINAMES_LOGDIR. Needed by omniNames.
		if (element instanceof OmniNames) {
			remoteCommand += "export OMNINAMES_LOGDIR=" + scratch + " ; ";
		}
		// Set OMNIORB_CONFIG. Needed by omniNames & all diet components.
		remoteCommand += "export OMNIORB_CONFIG=" + scratch
				+ "/omniORB4.cfg ; ";
		// Set all user Variables;
		List envVars = compRes.getCollection().getEnvVars();
		if (!envVars.isEmpty()) {
			for (java.util.Iterator it = envVars.iterator(); it.hasNext();) {
				EnvVar v = (EnvVar) it.next();
				remoteCommand += "export " + v.getName() + "=" + v.getValue()
						+ " ; ";
			}
		}
		// Get into correct directory. Needed by LogCentral and testTool.
		remoteCommand += "cd " + scratch + " ; ";
		// Provide resiliency to the return from ssh with nohup. Give binary.
		remoteCommand += "nohup " + element.getBinaryName() + " ";
		if (element instanceof Forwarder) {
			remoteCommand += "--net-config ";
		}
		// Provide config file name with full path. Needed by agents and seds.
		if ((element instanceof MasterAgent) || (element instanceof Ma_dag)
				|| (element instanceof LocalAgent)
				|| (element instanceof ServerDaemon)
				|| (element instanceof Forwarder)) {
			remoteCommand += scratch + "/" + element.getCfgFileName() + " ";
		}
		// Provide command line parameters. Needed by SeDs and Ma_dag only.
		if ((element instanceof ServerDaemon)
				&& (((ServerDaemon) element).getParameters() != null)) {
			remoteCommand += ((ServerDaemon) element).getParameters() + " ";
		}
		if ((element instanceof Ma_dag)
				&& (((Ma_dag) element).getParameters() != null)) {
			remoteCommand += ((Ma_dag) element).getParameters() + " ";
		}
		// Give -start parameter to omniNames.
		if (element instanceof OmniNames) {
			if (((OmniNames) element).getContact() != null) {
				/*
				 * If we are restarting omniNames, then we do not add the
				 * "-start" option.
				 */
				if (!((OmniNames) element).getBackupRestart()) {
					remoteCommand += "-start 2815 -ignoreport ";
				}
				remoteCommand += "-ORBendPoint giop:tcp:"
						+ ((OmniNames) element).getContact() + ":"
						+ ((OmniNames) element).getPort() + " ";
			} else /*
					 * If we are restarting omniNames, then we do not add the
					 * "-start" option.
					 */if (!((OmniNames) element).getBackupRestart()) {
				remoteCommand += "-start " + ((OmniNames) element).getPort()
						+ " ";
			}
			remoteCommand += "-nohostname ";
		}
		if (element.getName().compareTo("LogCentral") == 0) {
			remoteCommand += "-config " + element.getCfgFileName() + " ";
			if (compRes.getEndPointContact() != null) {
				remoteCommand += "-ORBendPoint giop:tcp:"
						+ compRes.getEndPointContact() + ":";
			} else if (compRes.getBegAllowedPorts() > 0) {
				remoteCommand += "-ORBendPoint giop:tcp::";
			}
			if (compRes.getBegAllowedPorts() > 0) {
				int port = compRes.allocateAllowedPort();
				if (port > 0) {
					remoteCommand += port;
				}
			}
			remoteCommand += " ";
		}
		// Redirect stdin/stdout/stderr so ssh can exit cleanly w/ live process
		remoteCommand += "< /dev/null ";
		if (!(runConfig.isSaveStdOut()) && !(runConfig.isSaveStdErr())) {
			remoteCommand += "> /dev/null 2>&1 ";
		} else {
			if (runConfig.isSaveStdOut()) {
				remoteCommand += "> " + element.getName() + ".out ";
			} else {
				remoteCommand += "> /dev/null ";
			}
			if (runConfig.isSaveStdErr()) {
				remoteCommand += "2> " + element.getName() + ".err ";
			} else {
				remoteCommand += "2> /dev/null ";
			}
		}
		// Background process and give correct quotes
		execSshGetPid(element, access, remoteCommand, runConfig, scratch);
		updateKillScript(element, access, killBackup);
	}

	// input: command ; command ; command
	private void execSshGetPid(Elements element, AccessMethod access,
			String remoteCommand, RunConfig runConfig, String scratch) {
		String newCommand = null;
		LaunchInfo launchInfo = element.getLaunchInfo();
		launchInfo.clearLastLaunch();

		newCommand = "( /bin/echo \"" + remoteCommand + "&\" ; ";
		newCommand += "/bin/echo '/bin/echo ${!}' ) | ";
		newCommand += "/usr/bin/ssh -q ";
		newCommand += access.getLogin() + "@" + access.getServer() + " ";
		newCommand += "\" tee " + scratch + "/" + element.getName()
				+ ".launch ";
		newCommand += "| /bin/sh - \"";

		String[] commandArray = { "/bin/sh", "-c", newCommand };

		for (int i = 0; (i < commandArray.length); i++) {
			consoleCtrl.printOutput("Command element " + i + " is "
					+ commandArray[i], 2);
		}

		try {
			// Run the process
			Process p = Runtime.getRuntime().exec(commandArray);

			// Get output and error from launch
			BufferedReader brErr = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));
			launchInfo.setLastLaunchStdErr(brErr.readLine());
			BufferedReader brOut = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			launchInfo.setLastLaunchStdOut(brOut.readLine());
		} catch (IOException x) {
			System.err.println("Launch of " + element.getName()
					+ " failed with following exception.");
			x.printStackTrace();
			launchInfo
					.setLaunchState(com.sysfera.godiet.Defaults.LAUNCH_STATE_FAILED);
			return;
		}

		if (launchInfo.getLastLaunchStdErr() != null) {
			System.err
					.println("Launch of " + element.getName()
							+ " failed with stdErr "
							+ launchInfo.getLastLaunchStdErr());
			launchInfo
					.setLaunchState(com.sysfera.godiet.Defaults.LAUNCH_STATE_CONFUSED);
		} else if (launchInfo.getLastLaunchStdOut() == null) {
			System.err.println("Launch of " + element.getName()
					+ " failed to return PID.");
			launchInfo
					.setLaunchState(com.sysfera.godiet.Defaults.LAUNCH_STATE_CONFUSED);
		} else {
			try {
				int pid = Integer.parseInt(launchInfo.getLastLaunchStdOut());
				consoleCtrl.printOutput("PID: " + pid, 2);
				launchInfo.setPID(pid);
				launchInfo
						.setLaunchState(com.sysfera.godiet.Defaults.LAUNCH_STATE_RUNNING);
			} catch (NumberFormatException x) {
				System.err.println("Launch of " + element.getName()
						+ " failed.");
				System.err.println("Could not parse PID in stdout: "
						+ launchInfo.getLastLaunchStdOut());
				launchInfo.setPID(-1);
				launchInfo
						.setLaunchState(com.sysfera.godiet.Defaults.LAUNCH_STATE_CONFUSED);
			}
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.sysfera.godiet.Utils.SshUtils#stopWithSsh(com.sysfera.godiet.Model
	 * .Elements, com.sysfera.godiet.Model.RunConfig, boolean)
	 */
	@Override
	public void stopWithSsh(Elements element, RunConfig runConfig) {
		ComputeResource compRes = element.getComputeResource();
		AccessMethod access = compRes.getAccessMethod("ssh");
		LaunchInfo launch = element.getLaunchInfo();
		if (access == null) {
			System.err.println("stopWithSsh: compRes does not have ssh access "
					+ "method. Ignoring stop request");
			return;
		}
		if (launch.getPID() <= 0) {
			System.err.println("stopWithSsh: no valid PID available for "
					+ element.getName() + ". Ignoring stop request.");
		}

		String stopJob;

		stopJob = "kill -9 " + element.getLaunchInfo().getPID();

		String[] commandStop = { "/usr/bin/ssh",
				access.getLogin() + "@" + access.getServer(), stopJob };

		for (int i = 0; (i < commandStop.length); i++) {
			consoleCtrl.printOutput("Command element " + i + " is "
					+ commandStop[i], 2);
		}
		try {
			Runtime.getRuntime().exec(commandStop);
			launch.setLaunchState(com.sysfera.godiet.Defaults.LAUNCH_STATE_STOPPED);
			launch.setLogState(com.sysfera.godiet.Defaults.LOG_STATE_STOPPED);
		} catch (IOException x) {
			consoleCtrl.printError("stopElement triggered an exception.", 0);
			x.printStackTrace();
		}
	}

	private void updateKillScript(Elements element, AccessMethod access,
			File killPlatformFile) {
		java.util.Date currTime = new java.util.Date();

		if (killPlatformFile == null) {
			consoleCtrl.printError("File for kill script unavailable.");
			return;
		}
		consoleCtrl.printOutput("Saving kill command for " + element.getName()
				+ " in " + killPlatformFile.getPath(), 3);
		try {
			FileWriter out = new FileWriter(killPlatformFile, true);
			out.write("\n# " + element.getName() + " launched at "
					+ currTime.toString() + "\n");
			out.write("/usr/bin/ssh " + access.getLogin() + "@"
					+ access.getServer() + " kill -9 "
					+ element.getLaunchInfo().getPID() + "\n");
			out.close();
		} catch (IOException x) {
			consoleCtrl.printError("Failed to write " + element.getName()
					+ " to " + killPlatformFile.getPath());
			x.printStackTrace();
		}
	}
}
