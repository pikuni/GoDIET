package com.sysfera.godiet.model.factories;

import java.util.List;
import java.util.Map;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.generics.ConfigurationBuildingException;
import com.sysfera.godiet.exceptions.remote.IncubateException;
import com.sysfera.godiet.managers.InfrastructureManager;
import com.sysfera.godiet.model.configurator.ConfigurationFile;
import com.sysfera.godiet.model.configurator.ConfigurationFileBuilderService;
import com.sysfera.godiet.model.generated.Binary;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Env;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Resource;
import com.sysfera.godiet.model.generated.Software;
import com.sysfera.godiet.model.generated.Ssh;
import com.sysfera.godiet.model.generated.Var;
import com.sysfera.godiet.model.softwares.OmniNamesManaged;
import com.sysfera.godiet.model.softwares.SoftwareController;
import com.sysfera.godiet.model.softwares.SoftwareManager;
import com.sysfera.godiet.model.validators.RuntimeValidator;

/**
 * Managed OmniNames factory
 * 
 * @author phi
 * 
 */

public class OmniNamesFactory {

	private static final String DEFAULT_BINARYNAME = "omniNames";
	private InfrastructureManager infrastructureManager;
	private final SoftwareController softwareController;
	private final RuntimeValidator<OmniNamesManaged> validator;
	private final ConfigurationFileBuilderService configurationFileBuilderService;

	public OmniNamesFactory(SoftwareController softwareController,
			RuntimeValidator<OmniNamesManaged> omniNamesValidator,
			ConfigurationFileBuilderService configurationFileBuilderService) {
		this.softwareController = softwareController;
		this.validator = omniNamesValidator;
		this.configurationFileBuilderService = configurationFileBuilderService;

	}

	public void setInfrastructureManager(
			InfrastructureManager infrastructureManager) {
		this.infrastructureManager = infrastructureManager;
	}

	/**
	 * Create a managed omninames given his description. Check validity. Set the
	 * default option if needed (like command launch).
	 * 
	 * 
	 * @param omniNamesDescription
	 * @return The managed omniNames
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 * @throws IncubateException
	 * @throws DietResourceCreationException
	 *             if the specified domain doesn't match domains on which it
	 *             pluggedOn
	 */

	public OmniNamesManaged create(OmniNames omniNamesDescription,
			Resource pluggedOn) throws DietResourceCreationException,
			IncubateException {
		Domain domain = infrastructureManager.getDomains(omniNamesDescription
				.getDomain());
		if (domain == null)
			throw new DietResourceCreationException(
					"Unable to find domain with the name : "
							+ omniNamesDescription.getDomain());

		List<Domain> domainsPluggedOn = infrastructureManager
				.getDomains(pluggedOn);
		if (!domainsPluggedOn.contains(domain)) {
			throw new DietResourceCreationException("The resource "
					+ pluggedOn.getId() + " isn't in "
					+ omniNamesDescription.getDomain());
		}
		Ssh listenSsh = infrastructureManager.getSsh(pluggedOn, domain);
		String listenAddress = listenSsh.getServer();
		OmniNamesManaged omniNamesManaged = new OmniNamesManaged(
				omniNamesDescription, pluggedOn, softwareController, validator);
		omniNamesManaged.setAddress(listenAddress);
		settingOmniNamesRunningCommand(omniNamesManaged);
		try {
			configurationFileBuilderService.build(omniNamesManaged);
		} catch (ConfigurationBuildingException e) {
			new IncubateException("Unable to create configurations file ", e);
		}

		return omniNamesManaged;
	}

	/**
	 * Build the omniNames running command
	 * OMNINAMES_LOGDIR={scratch_runtime}/{DomainName}/ +
	 * OMNIORB_CONFIG={scratch_runtime}/{omniNamesId}.cfg + nohup
	 * {OmniNamesBinary} + -start -always > {scratch_runtime}/OmniNames.out 2>
	 * {scratch_runtime}/OmniNames.err &
	 * 
	 * @param softManaged
	 * 
	 */
	private void settingOmniNamesRunningCommand(
			SoftwareManager<OmniNames> softManaged) {
		
		
		String command = "";
		String scratchDir = softManaged.getPluggedOn().getScratch().getDir();
		// Add all environment node
		Env env = softManaged.getPluggedOn().getEnv();
		if (env != null) {
			List<Var> vars = env.getVar();
			if (vars != null) {
				for (Var var : vars) {
					command += " " + var.getName() + "=" + var.getValue() + " ";
				}
			}
		}

		Software softwareDescription = softManaged.getSoftwareDescription();
		//TODO: do something better for default
		Binary b = new ObjectFactory().createBinary();
		b.setName(DEFAULT_BINARYNAME);
		softwareDescription.setBinary(b);
		//
		
		command += "OMNINAMES_LOGDIR=" + scratchDir + "/";
		command += " ";

		command += "OMNIORB_CONFIG=" + scratchDir + "/"
				+ softwareDescription.getId() + ".cfg";
		command += " nohup ";
		command += softwareDescription.getBinary().getName();

		command += " ";
		command += "-start -always >";
		command += scratchDir + "/OmniNames.out 2> ";
		command += scratchDir + "/OmniNames.err &";
		softManaged.setRunningCommand(command);
	}

}
