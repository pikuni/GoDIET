package com.sysfera.godiet.shell.command


import org.codehaus.groovy.tools.shell.ComplexCommandSupport
import org.codehaus.groovy.tools.shell.Shell
import org.codehaus.groovy.tools.shell.util.Preferences


import com.sysfera.godiet.common.model.SoftwareInterface;
import com.sysfera.godiet.common.model.generated.Software;
import com.sysfera.godiet.common.model.states.ResourceState;
import com.sysfera.godiet.common.model.states.ResourceState.State;
import com.sysfera.godiet.common.services.PlatformService;
import com.sysfera.godiet.shell.GoDietSh
import java.text.SimpleDateFormat;


/**
 * The status commands.
 *
 * 
 * @author phi
 */
class StatusCommand
extends ComplexCommandSupport {
	private static final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
	StatusCommand(final Shell shell) {
		super(shell, "status", "s")
		this.functions = [
			'services',
			'ma',
			'la',
			'sed',
			'all'
		]
	}

	private printStatus(String elementName, List<SoftwareInterface> resources) {
		if(resources.size() == 0) {
			io.out.println("\n@|bold No ${elementName}|@")
			return
		}
		io.out.println("")
		io.out.println("@|bold ${elementName}|@ Status (${resources.size()}) : ")
		io.out.println("@|bold Label\t\tStatus\t\tSince\t\tPlugged\t\tCause|@")
		io.out.println("---------------------------------------------------------")

		for (SoftwareInterface<? extends Software> resource : resources) {
			try{
				def coloredStatus
				String cause ='-'
				String since = sdf.format(resource.lastTransition)
				switch (resource.state) {
					case State.UP:
						coloredStatus =  "@|green ${resource.state}|@"
						break;
					case State.DOWN:
						coloredStatus =  "@|yellow ${resource.state}|@"
						break;
					case State.ERROR:
						coloredStatus =  "@|BG_RED,BLACK ${resource.state}|@"
						if(resource.errorMessage !=null)
							cause = resource.errorMessage
						else cause = "Not yet managed"
						break;
					default:
						coloredStatus = "@|BLUE ${resource.state}|@"
						break;
				}
				io.out.println "${resource.softwareDescription.id}\t${coloredStatus}\t${since}\t${resource.pluggedOn.id}\t${cause}"
			}catch (Exception e) {
				io.err.println("Status ${resource.softwareDescription.id} printing error",e)
			}
		}
	}

	def do_services = {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List omniNames = diet.omninames
		printStatus("OmniNames",omniNames)
	}

	def do_sed = {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService	
		List seds = diet.seds
		printStatus("Seds",seds)
	}
	def do_forwarders = {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List forwarders = diet.forwarders
		printStatus("Forwarders",forwarders)
	}
	def do_ma = {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List mas = diet.masterAgents
		printStatus("Master agents",mas)
	}
	def do_la = {
		PlatformService diet = ((GoDietSh)shell).godiet.platformService
		List las = diet.localAgents
		printStatus("Local agents",las)
	}
	def do_all = {
		do_services()
		do_forwarders()
		do_ma()
		do_la()
		do_sed()
	}
}
