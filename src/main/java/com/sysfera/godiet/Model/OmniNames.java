/*@GODIET_LICENSE*/
/*
 * OmniNames.java
 *
 * Created on 26 May 2004, 14:44
 */

package com.sysfera.godiet.Model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import com.sysfera.godiet.Model.physicalresources.ComputeResource;

/**
 * 
 * @author hdail
 */
public class OmniNames extends Services {
	private String contact = null;
	private int port = -1;
	private boolean backupRelaunch = false;

	// giopMaxMsgSize This value defines the ORB-wide limit on the size of GIOP
	// message
	// private long giopMaxMsgSize = 1073741824; // giopMaxMsgSize in octet
	// default = 1Go

	public OmniNames(String name, ComputeResource compRes, String binary,
			String contact, Domain domain) {
		super(name, compRes, binary, domain);
		this.contact = contact;
		this.port = 2809;
		initRef();
	}

	public OmniNames(String name, ComputeResource compRes, String binary,
			String contact, int port, Domain domain) {
		super(name, compRes, binary, domain);
		this.contact = contact;
		this.port = port;
		initRef();
	}

	private void initRef() {
		Option opt1 = new Option();
		opt1.setName("InitRef");
		opt1.setValue("NameService=corbaname::" + getContact() + ":"
				+ getPort());
		this.elConfig.addOption(opt1);
		Option opt2 = new Option();
		opt2.setName("supportBootstrapAgent");
		opt2.setValue("1");
		this.elConfig.addOption(opt2);

	}

	public String getContact() {
		return this.contact;
	}

	public int getPort() {
		return this.port;
	}

	public void writeCfgFile(FileWriter out) throws IOException {
		for (Iterator it = elConfig.getOptions().iterator(); it.hasNext();) {
			Option o = (Option) it.next();
			out.write(o.getName() + " = " + o.getValue() + "\n");
		}
	}

	public void setBackupRestart(boolean val) {
		this.backupRelaunch = val;
	}

	public boolean getBackupRestart() {
		return this.backupRelaunch;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}
}
