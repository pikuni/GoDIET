package com.sysfera.godiet.model.factories;

import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.model.DietResourceManaged;
import com.sysfera.godiet.model.generated.Config;
import com.sysfera.godiet.model.generated.Forwarder;
import com.sysfera.godiet.model.generated.Gateway;
import com.sysfera.godiet.model.generated.ObjectFactory;
import com.sysfera.godiet.model.generated.Options;
import com.sysfera.godiet.model.generated.Options.Option;

/**
 * Managed Forwarder factory
 * 
 * @author phi
 * 
 */
public class ForwarderFactory {
	private static String FORWARDERBINARY = "dietForwarder";

	public static enum ForwarderType {
		CLIENT("CLIENT"), SERVER("SERVER");
		public final String label;
		private ForwarderType(String label) {
			this.label = label;
		}	
		

	}


	/**
	 * TODO: Need to move this function. TODO: Why ?
	 * 
	 * @param gateway
	 * @param type
	 * @return
	 */
	public Forwarder create(Gateway gateway, ForwarderFactory.ForwarderType type) {
		Forwarder forwarder = new Forwarder();
		Config config = new Config();
		config.setServer(gateway.getRef());
		config.setRemoteBinary(FORWARDERBINARY);
		forwarder.setConfig(config);
		forwarder.setId("DietForwarder-" + gateway.getId() + "-" + type);
		switch (type) {
		case CLIENT:
			forwarder.setType("CLIENT");
			break;

		case SERVER:
			forwarder.setType("SERVER");
			break;

		default:
			break;
		}
		return forwarder;
	}

	/**
	 * Create a managed diet resource. Check description validity and add
	 * default parameters if needed.
	 * 
	 * @param forwarder Forwarder description
	 * @return The Managed forwarder
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	public DietResourceManaged create(Forwarder forwarder)
			throws DietResourceCreationException {

		DietResourceManaged dietResourceManaged = new DietResourceManaged();
		dietResourceManaged.setManagedSoftware(forwarder);
		settingConfigurationOptions(dietResourceManaged);

		return dietResourceManaged;
	}

	/**
	 * Init default value
	 * @param forwarder
	 * @throws DietResourceCreationException
	 *             if resource not plugged
	 */
	private void settingConfigurationOptions(DietResourceManaged forwarder)
			throws DietResourceCreationException {
		if (forwarder.getPluggedOn() == null) {
			throw new DietResourceCreationException(forwarder.getManagedSoftwareDescription()
					.getId() + " not plugged on physical resource");
		}

		ObjectFactory factory = new ObjectFactory();
		Options opts = factory.createOptions();

		Option accept = factory.createOptionsOption();
		accept.setKey("accept");
		accept.setValue(".*");
		Option reject = factory.createOptionsOption();
		reject.setKey("reject");
		reject.setValue(forwarder.getPluggedOn().getSsh().getServer());
		opts.getOption().add(accept);
		opts.getOption().add(reject);
		forwarder.getManagedSoftwareDescription().setCfgOptions(opts);

	}
}
