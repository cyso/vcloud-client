package nl.cyso.vcloud.client;


import nl.cyso.vcloud.client.types.ModeType;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.UnrecognizedOptionException;

import com.vmware.vcloud.sdk.Task;

public class Entry {
	public static void main(String[] args) {
		CommandLine cli = null;
		try {
			cli = new PosixParser().parse(Configuration.getOptions(), args);
		} catch (MissingArgumentException me) {
			Formatter.usageError(me.getLocalizedMessage(), Configuration.getOptions());
			System.exit(-1);
		} catch (MissingOptionException mo) {
			Formatter.usageError(mo.getLocalizedMessage(), Configuration.getOptions());
			System.exit(-1);
		} catch (AlreadySelectedException ase) {
			Formatter.usageError(ase.getLocalizedMessage(), Configuration.getOptions());
		} catch (UnrecognizedOptionException uoe) {
			Formatter.usageError(uoe.getLocalizedMessage(), Configuration.getOptions());
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (cli.hasOption("config")) {
			Configuration.loadFile(cli.getOptionValue("config"));
		}

		Configuration.load(cli);

		if (Configuration.getMode() == ModeType.HELP) {
			new HelpFormatter().printHelp("vcloud-client", Configuration.getOptions(), true);
			Formatter.printExamples();
			System.exit(0);
		} else if (Configuration.getMode() == ModeType.VERSION) {
			System.out.println(String.format("%s version %s\nBUILD_VERSION: %s", Version.PROJECT_NAME, Version.RELEASE_VERSION, Version.BUILD_VERSION));
			System.exit(0);
		}

		if (!Configuration.hasUsername() || !Configuration.hasPassword() || !Configuration.hasServer()) {
			Formatter.usageError("No credentials were set, or server uri was missing.", Configuration.getOptions());
		}

		vCloudClient client = new vCloudClient();
		client.login(Configuration.getServer(), Configuration.getUsername(), Configuration.getPassword());

		if (Configuration.getMode() == ModeType.LIST) {
			if (Configuration.getListType() == null) {
				Formatter.usageError("Invalid list type was selected.", Configuration.getOptions());
			}

			switch (Configuration.getListType()) {
			case ORG:
				client.listOrganizations();
				break;
			case VDC:
				if (!Configuration.hasOrganization()) {
					Formatter.usageError("An organization must also be specified when listing VDCs", Configuration.getOptions());
				}
				client.listVDCs(Configuration.getOrganization());
				break;
			case CATALOG:
				if (!Configuration.hasOrganization()) {
					Formatter.usageError("An organization must also be specified when listing Catalogs", Configuration.getOptions());
				}
				client.listCatalogs(Configuration.getOrganization());
				break;
			case VAPP:
				if (!Configuration.hasOrganization()) {
					Formatter.usageError("An organization must also be specified when listing vApps", Configuration.getOptions());
				}
				if (!Configuration.hasVDC()) {
					Formatter.usageError("A VDC must also be specified when listing vApps", Configuration.getOptions());
				}
				client.listVApps(Configuration.getOrganization(), Configuration.getVDC());
				break;
			case VM:
				if (!Configuration.hasOrganization()) {
					Formatter.usageError("An organization must also be specified when listing VMs", Configuration.getOptions());
				}
				if (!Configuration.hasVDC()) {
					Formatter.usageError("A VDC must also be specified when listing VMs", Configuration.getOptions());
				}
				if (!Configuration.hasVApp()) {
					Formatter.usageError("A vApp must also be specified when listing VMs", Configuration.getOptions());
				}
				client.listVMs(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp());
				break;
			default:
				System.err.println("Not yet implemented");
				break;
			}
		} else if (Configuration.getMode() == ModeType.ADDVM) {
			if (!Configuration.hasOrganization()) {
				Formatter.usageError("An existing organization has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVDC()) {
				Formatter.usageError("An existing virtual data center has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVApp()) {
				Formatter.usageError("An existing vApp has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasCatalog()) {
				Formatter.usageError("An existing Catalog has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasFqdn()) {
				Formatter.usageError("A FQDN has to be specified for the new VM", Configuration.getOptions());
			}
			if (!Configuration.hasDescription()) {
				Formatter.usageError("A description has to be specified for the new VM", Configuration.getOptions());
			}
			if (!Configuration.hasTemplate()) {
				Formatter.usageError("A template has to be specified for the new VM", Configuration.getOptions());
			}
			if (!Configuration.hasIp()) {
				Formatter.usageError("An IP has to be specified for the new VM", Configuration.getOptions());
			}
			if (!Configuration.hasNetwork()) {
				Formatter.usageError("A Network has to be specified for the new VM", Configuration.getOptions());
			}

			Formatter.waitForTaskCompletion(client.addVM(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getCatalog(), Configuration.getTemplate(), Configuration.getFqdn(), Configuration.getDescription(), Configuration.getIp().getHostAddress(), Configuration.getNetwork()));
		} else if (Configuration.getMode() == ModeType.REMOVEVM || Configuration.getMode() == ModeType.POWERONVM || Configuration.getMode() == ModeType.POWEROFFVM || Configuration.getMode() == ModeType.SHUTDOWNVM || Configuration.getMode() == ModeType.CONSOLIDATEVM) {
			if (!Configuration.hasOrganization()) {
				Formatter.usageError("An existing organization has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVDC()) {
				Formatter.usageError("An existing virtual data center has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVApp()) {
				Formatter.usageError("An existing vApp has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVM()) {
				Formatter.usageError("An existing VM has to be selected", Configuration.getOptions());
			}
			Task t = null;
			switch (Configuration.getMode()) {
			case REMOVEVM:
				t = client.removeVM(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getVM());
				break;
			case POWERONVM:
				t = client.powerOnVM(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getVM());
				break;
			case POWEROFFVM:
				t = client.powerOffVM(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getVM());
				break;
			case SHUTDOWNVM:
				t = client.shutdownVM(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getVM());
				break;
			case CONSOLIDATEVM:
				t = client.consolidateVM(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getVM());
			}
			Formatter.waitForTaskCompletion(t);
		} else if (Configuration.getMode() == ModeType.RESIZEDISK) {
			if (!Configuration.hasOrganization()) {
				Formatter.usageError("An existing organization has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVDC()) {
				Formatter.usageError("An existing virtual data center has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVApp()) {
				Formatter.usageError("An existing vApp has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasVM()) {
				Formatter.usageError("An existing VM has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasDiskName()) {
				Formatter.usageError("An existing disk has to be selected", Configuration.getOptions());
			}
			if (!Configuration.hasDiskSize()) {
				Formatter.usageError("An new disk size has to be specified", Configuration.getOptions());
			}
			Formatter.waitForTaskCompletion(client.resizeVMDisks(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getVM(), Configuration.getDiskName(), Configuration.getDiskSize()));
		} else {
			Formatter.usageError("No mode was selected", Configuration.getOptions());
		}
	}
}
