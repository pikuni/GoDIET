package com.sysfera.godiet.model.xml;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sysfera.godiet.command.LoadXMLImplCommand;
import com.sysfera.godiet.exceptions.CommandExecutionException;
import com.sysfera.godiet.managers.ResourcesManager;
import com.sysfera.godiet.utils.xml.XmlScannerJaxbImpl;

public class DietResourceManagerTest {
	private Logger log = LoggerFactory.getLogger(getClass());
	private ResourcesManager rm;
	@Before
	public void initRM() {
		String testCaseFile = "exampleLocalhost.xml";
		InputStream inputStream = getClass().getClassLoader()
				.getResourceAsStream(testCaseFile);
		rm = new ResourcesManager();
		XmlScannerJaxbImpl scanner = new XmlScannerJaxbImpl();
		LoadXMLImplCommand xmlLoadingCommand = new LoadXMLImplCommand();
		xmlLoadingCommand.setRm(rm);
		xmlLoadingCommand.setXmlInput(inputStream);
		xmlLoadingCommand.setXmlParser(scanner);

		try {
			xmlLoadingCommand.execute();

		} catch (CommandExecutionException e) {
			log.error("Test Fail", e);
			Assert.fail();
		}

	}
	
	@Test
	public void testStart()
	{
		
	}
}
