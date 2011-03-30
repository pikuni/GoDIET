package com.sysfera.godiet.command;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.DietResourceCreationException;
import com.sysfera.godiet.exceptions.XMLParseException;
import com.sysfera.godiet.exceptions.graph.GraphDataException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.model.generated.Cluster;
import com.sysfera.godiet.model.generated.DietDescription;
import com.sysfera.godiet.model.generated.DietInfrastructure;
import com.sysfera.godiet.model.generated.DietServices;
import com.sysfera.godiet.model.generated.Domain;
import com.sysfera.godiet.model.generated.Infrastructure;
import com.sysfera.godiet.model.generated.Link;
import com.sysfera.godiet.model.generated.LocalAgent;
import com.sysfera.godiet.model.generated.MasterAgent;
import com.sysfera.godiet.model.generated.OmniNames;
import com.sysfera.godiet.model.generated.Sed;
import com.sysfera.godiet.utils.xml.XMLParser;

/**
 * Initialize resource manager with the given XML description file. Load
 * configuration. Load physical platform. Load diet agents. Dummy diet
 * forwarder loading ( 2 diet forwarder for each declared link )
 * 
 * @author phi
 * 
 */
public class CommandLoadXMLImpl implements Command {
	private Logger log = LoggerFactory.getLogger(getClass());

	private ResourcesManager rm;
	private XMLParser xmlScanner;
	private InputStream xmlInput;

	@Override
	public String getDescription() {
		return "Initialize resource manager given a  XML data source";
	}

	@Override
	public void execute() throws CommandExecutionException {
		log.debug("Enter in "
				+ Thread.currentThread().getStackTrace()[2].getMethodName()
				+ " method");
		if (rm == null || xmlScanner == null || xmlInput == null) {
			throw new CommandExecutionException(getClass().getName()
					+ " not initialized correctly");
		}
		try {
			DietDescription dietDescription = xmlScanner
					.buildDietModel(xmlInput);
			load(dietDescription);

		} catch (IOException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (XMLParseException e) {
			throw new CommandExecutionException("XML read error", e);
		} catch (DietResourceCreationException e) {
			throw new CommandExecutionException("Unable load model", e);
		}

	}

	/**
	 * Set the resource manager
	 * 
	 * @param rm
	 *            Resource Manager
	 */
	public void setRm(ResourcesManager rm) {
		this.rm = rm;
	}

	/**
	 * Set the Xml Scanner
	 * 
	 * @param xmlScanner
	 */
	public void setXmlParser(XMLParser parser) {
		this.xmlScanner = parser;
	}

	/**
	 * Input data source
	 * 
	 * @param xmlInput
	 *            the xmlInput to set
	 */
	public void setXmlInput(InputStream xmlInput) {
		this.xmlInput = xmlInput;
	}

	/**
	 * Initialize goDiet configuration Initialize platform (Physical resource)
	 * Initialize diet platform (diet agents, diet services, seds) Reset all
	 * model and load DietConfigurtion
	 * 
	 * @throws DietResourceCreationException
	 * @throws CommandExecutionException 
	 */
	private void load(DietDescription dietConfiguration)
			throws DietResourceCreationException, CommandExecutionException {
		if (dietConfiguration != null) {
			this.rm.setGoDietConfiguration(dietConfiguration
					.getGoDietConfiguration());
			initPlatform(dietConfiguration.getInfrastructure());
			initDietPlatform(dietConfiguration.getDietInfrastructure(),
					dietConfiguration.getDietServices());
		}

	}

	/**
	 * 
	 * @param infrastructure
	 * @throws CommandExecutionException 
	 */
	private void initPlatform(Infrastructure infrastructure) throws CommandExecutionException {
		List<Domain> domains = infrastructure.getDomain();
		this.rm.getPlatformModel().addDomains(domains);
		if (domains != null) {
			for (Domain domain : domains) {
				this.rm.getPlatformModel().addGateways(domain.getGateway());
				this.rm.getPlatformModel().addNodes(domain.getNode());
				List<Cluster> clusters = domain.getCluster();
				this.rm.getPlatformModel().addClusters(clusters);
				if (clusters != null) {
					for (Cluster cluster : clusters) {
						this.rm.getPlatformModel().addNodes(
								cluster.getComputingNode());
						this.rm.getPlatformModel().addFrontends(
								cluster.getFronted());
					}
				}
			}
		}

		List<Link> links = infrastructure.getLink();
		try {
			this.rm.getPlatformModel().addLinks(links);
		} catch (GraphDataException e) {
			
			throw new CommandExecutionException("Unable to add links. Error in the model description",e);
		}
	}

	/**
	 * Initialize the Diet platform given a DietInfrastructure andn DietService
	 * 
	 * @param dietHierarchy
	 * @param dietServices
	 * @throws DietResourceCreationException
	 */
	private void initDietPlatform(DietInfrastructure dietHierarchy,
			DietServices dietServices) throws DietResourceCreationException {
		List<OmniNames> omniNames = dietServices.getOmniNames();
		for (OmniNames omniName : omniNames) {
			this.rm.getDietModel().addOmniName(omniName);

		}
		initMasterAgent(dietHierarchy.getMasterAgent());

	}

	/**
	 * Init masterAgents. Deep tree search on LocalAgent.
	 * 
	 * @param List
	 *            of masterAgent
	 * @throws DietResourceCreationException
	 */
	private void initMasterAgent(List<MasterAgent> masterAgents)
			throws DietResourceCreationException {
		if (masterAgents != null) {
			for (MasterAgent masterAgent : masterAgents) {
				this.rm.getDietModel().addMasterAgent(masterAgent);
				initSeds(masterAgent.getSed());
				initLocalAgents(masterAgent.getLocalAgent());
			}

		}
	}

	/**
	 * Recursive call on LocalAgents.
	 * 
	 * @param localAgent
	 * @throws DietResourceCreationException
	 */
	private void initLocalAgents(List<LocalAgent> localAgents)
			throws DietResourceCreationException {
		if (localAgents != null) {
			for (LocalAgent localAgent : localAgents) {
				this.rm.getDietModel().addLocalAgent(localAgent);
				initSeds(localAgent.getSed());
				initLocalAgents(localAgent.getLocalAgent());
			}

		}
	}

	/**
	 * 
	 * @param sed
	 * @throws DietResourceCreationException
	 */
	private void initSeds(List<Sed> seds) throws DietResourceCreationException {
		if (seds != null) {
			for (Sed sed : seds) {
				this.rm.getDietModel().addSed(sed);
			}

		}

	}

}