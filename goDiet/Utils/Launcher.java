/*@GODIET_LICENSE*/
/*
 * Launcher.java
 *
 * Created on April 19, 2004, 1:59 PM
 */

package goDiet.Utils;

import goDiet.Model.AccessMethod;
import goDiet.Model.ComputeCollection;
import goDiet.Model.ComputeResource;
import goDiet.Model.Elements;
import goDiet.Model.Agents;
import goDiet.Model.ServerDaemon;
import goDiet.Model.OmniNames;
import goDiet.Model.RunConfig;
import goDiet.Model.StorageResource;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author  hdail
 */
public class Launcher {
    private goDiet.Controller.ConsoleController consoleCtrl;
    private File killPlatformFile;
    
    /** Creates a new instance of Launcher */
    public Launcher(goDiet.Controller.ConsoleController consoleController) {
        this.consoleCtrl=consoleController;
    }
    
    public void createLocalScratch() {
        RunConfig runCfg = consoleCtrl.getRunConfig();
        File dirHdl;
        String runLabel = null;
        
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMMdd_HHmm");
        java.util.Date today = new Date();
        String dateString = formatter.format(today);
        
        if(runCfg.useUniqueDirs){
            runLabel = "run_" + dateString;
            dirHdl = new File(runCfg.getLocalScratchBase(),runLabel);
            if( runCfg.useUniqueDirs && dirHdl.exists() ) {
                int i = 0;
                do {
                    i++;
                    dirHdl = new File(runCfg.getLocalScratchBase(),
                            runLabel + "_r" + i);
                } while (dirHdl.exists());
                runLabel += "_r" + i;
            }
            dirHdl.mkdirs();
            runCfg.setLocalScratch(runCfg.getLocalScratchBase() + "/" + runLabel);
            runCfg.setRunLabel(runLabel);
        } else {
            dirHdl = new File(runCfg.getLocalScratchBase());
            dirHdl.mkdirs();
            runCfg.setLocalScratch(runCfg.getLocalScratchBase());
            runCfg.setRunLabel(null);
        }
        
        runCfg.setLocalScratchReady(true);
        consoleCtrl.printOutput("\nLocal scratch directory ready:\n\t" +
                runCfg.getLocalScratch(),1);
        
        // Initiate output file for pids to use as backup for failures
        killPlatformFile = new File(runCfg.getLocalScratch(),
                "killPlatform.csh");
        try {
            if(killPlatformFile.exists()){
                killPlatformFile.delete();
            }
            killPlatformFile.createNewFile();
        } catch (IOException x){
            consoleCtrl.printOutput("createLocalScratch: Could not create " +
                    killPlatformFile);
        }
    }
    
    /* launchElement is the primary method for launching components of the DIET
     * hierarchy.  This method performs the following actions:
     *      - check that element, compRes, & scratch base are non-null
     *      - create the config file locally
     *      - stage the config file to remote host
     *      - run the element on the remote host
     */
    public void launchElement(Elements element,
            boolean useLogService){
        RunConfig runCfg = consoleCtrl.getRunConfig();
        if(element == null){
            consoleCtrl.printError("launchElement called with null element. " +
                    "Launch request ignored.", 1);
            return;
        }
        if(element.getComputeResource() == null){
            consoleCtrl.printError("LaunchElement called with null resource. " +
                    "Launch request ignored.");
            return;
        }
        if(runCfg.isLocalScratchReady() == false){
            consoleCtrl.printError("launchElement: Scratch space is not ready. " +
                    "Need to run createLocalScratch.");
            return;
        }
        
        consoleCtrl.printOutput("\n** Launching element " +
                element.getName() + " on " +
                element.getComputeResource().getName(),1);
        try {
            // LAUNCH STAGE 1: Write config file
            createCfgFile(element, useLogService);            
        } catch (IOException x) {
            consoleCtrl.printError("Exception writing cfg file for " +
                    element.getName(), 0);
            consoleCtrl.printError("Exception: " + x + "\nExiting.", 1);
            element.getLaunchInfo().setLaunchState(
                    goDiet.Defaults.LAUNCH_STATE_CONFUSED);
            System.exit(1);     /// TODO: Add error handling and don't exit
        }
        ComputeCollection coll = element.getComputeResource().getCollection();
        StorageResource storeRes = coll.getStorageResource();
        // LAUNCH STAGE 2: Stage config file
        stageFile(element.getCfgFileName(),storeRes);
        // LAUNCH STAGE 3: Launch element
        runElement(element);
    }
    /* launchElement2 is the second method for launching components of the DIET
     * hierarchy.  This method performs the following actions:
     *      - check that element, compRes, & scratch base are non-null
     *      - run the element on the remote host
     *      - don't need to create and stage cfg file, it suppose to be already done.
     */
    public void launchElement2(Elements element,
            boolean useLogService){
        if(element == null){
            consoleCtrl.printError("launchElement called with null element. " +
                    "Launch request ignored.", 1);
            return;
        }
        if(element.getComputeResource() == null){
            consoleCtrl.printError("LaunchElement called with null resource. " +
                    "Launch request ignored.");
            return;
        }
        consoleCtrl.printOutput("\n** Launching element " +
                element.getName() + " on " +
                element.getComputeResource().getName(),1);
        // LAUNCH STAGE 1: Launch element
        runElement(element);
    }
    // TODO: incorporate Elagi usage
    public void stageFile(String filename,StorageResource storeRes) {
        consoleCtrl.printOutput("Staging file " + filename + " to " +
                storeRes.getName(),1);
        
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        sshUtil.stageWithScp(filename,storeRes,consoleCtrl.getRunConfig());
    }
    // TODO: incorporate Elagi usage
    public void stageAllFile(StorageResource storeRes) {
        consoleCtrl.printOutput("Staging file to " +
                storeRes.getName(),1);
        
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        sshUtil.stageFilesWithScp(storeRes,consoleCtrl.getRunConfig());
    }
    // TODO: incorporate Elagi usage
    private void runElement(Elements element) {
        ComputeResource compRes = element.getComputeResource();
        StorageResource storage = compRes.getCollection().getStorageResource();
        consoleCtrl.printOutput("Executing element " + element.getName() +
                " on resource " + compRes.getName(),1);
        AccessMethod access = compRes.getAccessMethod("ssh");
        if(access == null){
            consoleCtrl.printError("runElement: compRes does not have " +
                    "ssh access method. Ignoring launch request.");
            return;
        }
        
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        sshUtil.runWithSsh(element,consoleCtrl.getRunConfig(),killPlatformFile);
    }
    
    public void stopElement(Elements element){
        consoleCtrl.printOutput("Trying to stop element " + element.getName(),1);
        SshUtils sshUtil = new SshUtils(consoleCtrl);
        if (element instanceof Agents || element instanceof ServerDaemon)
            sshUtil.stopWithSsh(element,consoleCtrl.getRunConfig(), true);
        else
            sshUtil.stopWithSsh(element,consoleCtrl.getRunConfig(), false);
    }
    
    public void createCfgFile(Elements element,
            boolean useLogService) throws IOException {
        RunConfig runCfg = consoleCtrl.getRunConfig();
        if (element != null){
            if( element.getName().compareTo("TestTool") == 0){
                return;
            }                                                
            File cfgFile = new File(runCfg.getLocalScratch(),
                    element.getCfgFileName());
            try {                                
                cfgFile.createNewFile();
                consoleCtrl.printOutput("Writing config file " +
                    element.getCfgFileName(),1);
                FileWriter out = new FileWriter(cfgFile); 
                element.writeCfgFile(out);                
                out.close();
            } catch (IOException x) {
                consoleCtrl.printError("Failed to write " + cfgFile.getPath());                
                throw x;
            }
        }        
    }
    
    private void writeCfgFileLogCentral(Elements element,FileWriter out) throws IOException {
          element.writeCfgFile(out);
    }
    
    private void writeCfgFileOmniNames(OmniNames omni,FileWriter out) throws IOException {
        omni.writeCfgFile(out);        
    }
    
    private void writeCfgFileDiet(Elements element,FileWriter out) throws IOException {        
        element.writeCfgFile(out);        
    }
}
