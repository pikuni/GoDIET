package com.sysfera.godiet.command;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.remote.RemoteAccess;
import com.sysfera.godiet.remote.RemoteAccessMock;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class CommandLaunchServicesTest {

	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;
	RemoteAccess remoteAccess = new RemoteAccessMock();

	@Before
	public void init() {

		// Init RM
		String testCaseFile = "testbed.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLImplCommand xmlLoadingCommand = new LoadXMLImplCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);
		xmlLoadingCommand.setRemoteAccess(remoteAccess);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testLaunchBeforePrepare() {
		StartServicesCommand launchServicesCommand = new StartServicesCommand();
		launchServicesCommand.setRm(rm);

		Exception commandExecException = null;

		try {
			launchServicesCommand.execute();
		} catch (CommandExecutionException e) {
			commandExecException = e;
		}
		// ass0et the exception object
		Assert.assertNotNull("No expected exception", commandExecException);
	}

	@Test
	public void testLaunch() {
		PrepareServicesCommand prepareCommand = new PrepareServicesCommand();
		prepareCommand.setRm(rm);
		StartServicesCommand launchServicesCommand = new StartServicesCommand();
		launchServicesCommand.setRm(rm);
		try {
			prepareCommand.execute();
			launchServicesCommand.execute();
		} catch (CommandExecutionException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
	}
}
