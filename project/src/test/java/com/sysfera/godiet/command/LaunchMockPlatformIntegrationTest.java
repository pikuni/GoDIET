package com.sysfera.godiet.command;

import java.io.InputStream;
import java.net.URL;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sysfera.godiet.command.init.InitForwardersCommand;
import com.sysfera.godiet.command.init.util.XMLLoadingHelper;
import com.sysfera.godiet.command.prepare.PrepareAgentsCommand;
import com.sysfera.godiet.command.prepare.PrepareServicesCommand;
import com.sysfera.godiet.command.start.StartAgentsCommand;
import com.sysfera.godiet.command.start.StartForwardersCommand;
import com.sysfera.godiet.command.start.StartServicesCommand;
import com.sysfera.godiet.command.stop.StopAgentsCommand;
import com.sysfera.godiet.command.stop.StopForwardersCommand;
import com.sysfera.godiet.command.stop.StopServicesCommand;
import com.sysfera.godiet.command.xml.LoadXMLDietCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.exceptions.remote.AddAuthentificationException;
import com.sysfera.godiet.managers.DietManager;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.managers.user.SSHKeyManager;
import com.sysfera.godiet.model.SoftwareController;
import com.sysfera.godiet.model.factories.GodietMetaFactory;
import com.sysfera.godiet.model.generated.User;
import com.sysfera.godiet.model.validators.ForwarderRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.LocalAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.MasterAgentRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.OmniNamesRuntimeValidatorImpl;
import com.sysfera.godiet.model.validators.SedRuntimeValidatorImpl;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteConfigurationHelper;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(inheritLocations = false, locations = {
		"/spring/spring-config.xml", "/spring/ssh-context.xml" })
public class LaunchMockPlatformIntegrationTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	private ResourcesManager rm;

	GodietMetaFactory godietAbstractFactory;
	@Autowired
	RemoteAccess remoteAccess;

	@Before
	public void init() {

		try {
			// Loading configuration
			{
				String configurationFile = "configuration/configuration-localhost.xml";

				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(configurationFile);
				XMLLoadingHelper.initConfig(rm, inputStream);

			}
			{
				String platformTestCase = "infrastructure/localhost-infrastructure.xml";
				InputStream inputStreamPlatform = getClass().getClassLoader()
						.getResourceAsStream(platformTestCase);
				XMLLoadingHelper.initInfrastructure(rm, inputStreamPlatform);
			}
			{
				// Init RM
				String testCaseFile = "diet/localhost-diet.xml";
				InputStream inputStream = getClass().getClassLoader()
						.getResourceAsStream(testCaseFile);
				XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
				LoadXMLDietCommand xmlLoadingCommand = new LoadXMLDietCommand();
				xmlLoadingCommand.setRm(rm);
				xmlLoadingCommand.setXmlInput(inputStream);
				xmlLoadingCommand.setXmlParser(scanner);

				SoftwareController softwareController = new RemoteConfigurationHelper(
						rm.getGodietConfiguration(),
						rm.getInfrastructureModel());

				DietManager dietModel = rm.getDietModel();
				godietAbstractFactory = new GodietMetaFactory(
						softwareController, new ForwarderRuntimeValidatorImpl(
								dietModel),
						new MasterAgentRuntimeValidatorImpl(dietModel),
						new LocalAgentRuntimeValidatorImpl(dietModel),
						new SedRuntimeValidatorImpl(dietModel),
						new OmniNamesRuntimeValidatorImpl(dietModel));
				xmlLoadingCommand.setAbstractFactory(godietAbstractFactory);

				xmlLoadingCommand.execute();

			}
		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
		String fakeKey = "fakeuser/testbedKey";
		URL urlFile = getClass().getClassLoader().getResource(fakeKey);
		if (urlFile == null || urlFile.getFile().isEmpty())
			Assert.fail("SSH key not found");

		try {
			User.Ssh.Key sshDesc = new User.Ssh.Key();
			sshDesc.setPath(urlFile.getPath());
			SSHKeyManager sshkey = new SSHKeyManager(sshDesc);
			sshkey.setPassword("godiet");
			this.rm.getUserManager().addManagedSSHKey(
					new SSHKeyManager(sshDesc));
			remoteAccess.addItentity(sshkey);
		} catch (AddAuthentificationException e) {
			Assert.fail("Unable to load testbed key");
		}

	}

	/***
	 * Launch all the elements described in the XML file
	 */
	@Test
	public void launchPlatform() {
		// Services commands
		PrepareServicesCommand prepareCommand = new PrepareServicesCommand();
		prepareCommand.setRm(rm);
		StartServicesCommand launchServicesCommand = new StartServicesCommand();
		launchServicesCommand.setRm(rm);
		StopServicesCommand stopServicesCommand = new StopServicesCommand();
		stopServicesCommand.setRm(rm);

		// Agents commands
		InitForwardersCommand initForwardersCommand = new InitForwardersCommand();
		initForwardersCommand.setRm(rm);
		initForwardersCommand.setForwarderFactory(godietAbstractFactory);
		PrepareAgentsCommand prepareAgents = new PrepareAgentsCommand();
		prepareAgents.setRm(rm);

		StartForwardersCommand launchForwarders = new StartForwardersCommand();
		launchForwarders.setRm(rm);
		StopForwardersCommand stopForwarders = new StopForwardersCommand();
		stopForwarders.setRm(rm);

		StartAgentsCommand startMas = new StartAgentsCommand();
		startMas.setRm(rm);
		StopAgentsCommand stopAgents = new StopAgentsCommand();
		stopAgents.setRm(rm);

		try {
			prepareCommand.execute();
			launchServicesCommand.execute();

			initForwardersCommand.execute();

			prepareAgents.execute();

			launchForwarders.execute();
			startMas.execute();

		} catch (CommandExecutionException e) {
			log.error(e.getMessage());
			Assert.fail(e.getMessage());
		} finally {
			try {
				stopAgents.execute();
			} catch (CommandExecutionException e) {
				log.error(e.getMessage());
				Assert.fail(e.getMessage());

			} finally {
				try {
					stopForwarders.execute();
				} catch (CommandExecutionException e) {
					log.error(e.getMessage());
					Assert.fail(e.getMessage());
				} finally {
					try {
						stopServicesCommand.execute();
					} catch (CommandExecutionException e) {
						log.error(e.getMessage());
						Assert.fail(e.getMessage());
					}
				}
			}
		}

	}
}
