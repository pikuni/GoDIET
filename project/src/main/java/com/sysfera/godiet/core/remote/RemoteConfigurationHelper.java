package com.sysfera.godiet.core.remote;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sysfera.godiet.common.exceptions.generics.PathException;
import com.sysfera.godiet.common.exceptions.generics.RemoteAccessException;
import com.sysfera.godiet.common.exceptions.remote.CheckException;
import com.sysfera.godiet.common.exceptions.remote.LaunchException;
import com.sysfera.godiet.common.exceptions.remote.PrepareException;
import com.sysfera.godiet.common.exceptions.remote.StopException;
import com.sysfera.godiet.common.model.generated.Node;
import com.sysfera.godiet.common.model.generated.Resource;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.core.managers.ConfigurationManager;
import com.sysfera.godiet.core.managers.InfrastructureManager;
import com.sysfera.godiet.core.managers.topology.infrastructure.Path;
import com.sysfera.godiet.core.model.configurator.ConfigurationFileImpl;
import com.sysfera.godiet.core.model.softwares.SoftwareController;
import com.sysfera.godiet.core.model.softwares.SoftwareManager;

/**
 * Agent configuration and remote access helper.
 * 
 * @author phi
 * 
 */
@Component
public class RemoteConfigurationHelper implements SoftwareController {
	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private RemoteAccess remoteAccess;

	@Autowired
	private ConfigurationManager configuration;
	@Autowired
	private InfrastructureManager platform;

	/**
	 * Prepare physical resource to launch the Software - Search the physical
	 * resource to run the diet agent - Create remote directory - Create
	 * configuration file on local scratch directory - Copy configuration files
	 * on remote physical resource
	 * 
	 * @param managedSoftware
	 * 
	 * @throws PrepareException
	 *             if create local files or can't copy files on remote host.
	 */
	@Override
	public void configure(SoftwareManager<? extends Software> managedSoftware)
			throws PrepareException {

		// the remote physical node to configure
		Resource remoteNode = managedSoftware.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new PrepareException(
					"Resource not plugged on physial resource");
		}

		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration
				.getLocalNodeId());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new PrepareException("Unable to find the resource: "
					+ configuration.getLocalNodeId());
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath(localNode, remoteNode);
		} catch (PathException e1) {
			throw new PrepareException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new PrepareException("Path node found");
		}

		String command = "";
		try {
			// Create Remote Directory
			// TODO: Do that in a init platform method
			command = "mkdir -p " + remoteNode.getScratch().getDir();
			remoteAccess.launch(command, path);

			// Copy file on remote host
			Collection<ConfigurationFileImpl> cfiles = managedSoftware
					.getConfigurationFiles().values();
			for (ConfigurationFileImpl configurationFile : cfiles) {
				// TODO: See fixme lack of design
				if (!configurationFile.isCopiedOn(remoteNode)) {
					log.debug("Copy file " + configurationFile.getId() + " on "
							+ remoteNode.getId());
					remoteAccess.copy(configurationFile, remoteNode
							.getScratch().getDir(), path);
					configurationFile.copied(remoteNode);
				} else {
					log.debug("File " + configurationFile.getId()
							+ " already copied on " + remoteNode.getId());
				}
			}

		} catch (RemoteAccessException e) {
			log.error("Unable to configure "
					+ managedSoftware.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " commmand " + command, e);
			throw new PrepareException("Unable to run configure "
					+ managedSoftware.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " .Commmand: " + command, e);
		}

	}

	/**
	 * Launch software on the physical resource - Search the physical resource
	 * to run the diet agent - Launch command
	 * 
	 * @param managedSofware
	 * @throws LaunchException
	 *             if can't connect to the remote host or can't launch binary
	 */
	@Override
	public void launch(SoftwareManager managedSofware) throws LaunchException {

		// the remote physical node to configure
		Resource remoteNode = managedSofware.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new LaunchException(
					"Resource not plugged on physial resource");
		}
		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration
				.getLocalNodeId());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new LaunchException(
					"Unable to find the resource from which the remote command is call");
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath(localNode, remoteNode);
		} catch (PathException e1) {
			throw new LaunchException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new LaunchException("Path node found");
		}

		// End of duplicate code

		String command = managedSofware.getRunningCommand();
		try {
			Integer pid = remoteAccess.launch(command, path);
			managedSofware.setPid(pid);
			log.info("Command " + command + " run with pid " + pid);
		} catch (RemoteAccessException e) {
			log.error("Unable to configure "
					+ managedSofware.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " commmand " + command, e);
			throw new LaunchException("Unable to run configure "
					+ managedSofware.getSoftwareDescription().getId() + " on "
					+ remoteNode.getId() + " .Commmand: " + command, e);
		}

	}

	/**
	 * Stop software on the physical resource - Search the physical resource to
	 * stop the diet agent - Launch stop command (actually kill but hope change
	 * :) )
	 * 
	 * @param resource
	 * @throws LaunchException
	 *             if can't connect to the remote host or can't launch binary
	 */
	@Override
	public void stop(SoftwareManager resource) throws StopException {

		// TODO: Duplicate code with configure, start

		// the remote physical node to configure
		Resource remoteNode = resource.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new StopException("Resource not plugged on physial resource");
		}

		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration
				.getLocalNodeId());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new StopException("Unable to find the resource: "
					+ configuration.getLocalNodeId());
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath(localNode, remoteNode);
		} catch (PathException e1) {
			throw new StopException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new StopException("Path node found");
		}

		Integer pid = resource.getPid();
		if (pid == null) {
			throw new StopException("Unable to kill "
					+ resource.getSoftwareDescription().getId()
					+ ". Pid is null");
		}

		// End of duplicate code
		String command = "kill " + pid;

		try {
			remoteAccess.launch(command, path);

			// TODO: Check if always run
		} catch (RemoteAccessException e) {
			throw new StopException("Unable to kill "
					+ resource.getSoftwareDescription().getId(), e);
		}

	}

	/**
	 * Check if the software running
	 * 
	 * @param managed
	 */
	@Override
	public void check(SoftwareManager resource) throws CheckException {
		// TODO: 99% Duplicate code with stop :) GENIAL !

		// the remote physical node to configure
		Resource remoteNode = resource.getPluggedOn();
		if (remoteNode == null) {
			log.error("Unable to configure remote resource. Resource not plugged on physial resource");
			throw new CheckException("Resource not plugged on physial resource");
		}

		// the local node. From where the command is launch.
		// TODO : Path findpath(FromDomain, ToNode); Move this code
		Resource localNode = platform.getResource(configuration
				.getLocalNodeId());
		if (localNode == null || !(localNode instanceof Node)) {
			log.error("Unable to find the local resource.");
			throw new CheckException("Unable to find the resource: "
					+ configuration.getLocalNodeId());
		}
		// Find a path between the current node until remote node
		Path path = null;
		try {
			path = platform.findPath(localNode, remoteNode);
		} catch (PathException e1) {
			throw new CheckException("", e1);
		}
		if (path == null) {
			log.error("Unable to configure remote resource. Unable to find a path");
			throw new CheckException("Path node found");
		}

		Integer pid = resource.getPid();
		if (pid == null) {
			throw new CheckException("Unable to check "
					+ resource.getSoftwareDescription().getId()
					+ ". Pid is null");
		}

		// End of duplicate code
		String command = "kill -0 " + pid;

		try {
			remoteAccess.check(command, path);

			// TODO: Check if always run
		} catch (RemoteAccessException e) {
			throw new CheckException("Unable to check "
					+ resource.getSoftwareDescription().getId(), e);
		}

	}

}
