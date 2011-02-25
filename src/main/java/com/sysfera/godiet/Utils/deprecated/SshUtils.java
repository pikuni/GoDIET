package com.sysfera.godiet.Utils.deprecated;

import java.io.File;

import com.sysfera.godiet.Model.deprecated.Elements;
import com.sysfera.godiet.Model.deprecated.RunConfig;
import com.sysfera.godiet.Model.physicalresources.deprecated.StorageResource;
import com.sysfera.godiet.exceptions.LaunchException;
/**
 * Utils class provide commodity with SSH
 * 
 * @author phi
 *
 */
public interface SshUtils {

	public abstract void stageWithScp(String filename,
			StorageResource storeRes, RunConfig runConfig) throws LaunchException;

	public abstract void stageFilesWithScp(StorageResource storeRes,
			RunConfig runConfig) throws LaunchException;

	// TODO: incorporate Elagi usage
	public abstract void runWithSsh(Elements element, RunConfig runConfig,
			File killBackup);

	public abstract void stopWithSsh(Elements element, RunConfig runConfig);

}