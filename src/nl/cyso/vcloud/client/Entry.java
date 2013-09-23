/*
 * Copyright (c) 2013 Cyso < development [at] cyso . nl >
 *
 * This file is part of vcloud-client.
 *
 * vcloud-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * vcloud-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with vcloud-client. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.cyso.vcloud.client;

import nl.cyso.vcloud.client.config.ConfigModes;
import nl.cyso.vcloud.client.config.Configuration;
import nl.cyso.vcloud.client.types.ModeType;

import org.apache.commons.cli.CommandLine;

import com.vmware.vcloud.sdk.Task;

public class Entry {

	public static void main(String[] args) {

		// Start out in ROOT ConfigMode
		CommandLine cli = null;

		// Try to parse all ROOT cli options
		cli = Configuration.parseCli(ModeType.ROOT, args);

		// Load the config if it was specified
		if (cli.hasOption("config")) {
			Configuration.loadFile(cli.getOptionValue("config"));
		}

		// Load all options parsed for ROOT ConfigMode
		Configuration.load(cli);

		// Now we know which ConfigMode was selected

		// Display (specific) help
		if (Configuration.getMode() == ModeType.HELP) {
			if (Configuration.hasHelpType()) {
				ConfigModes.printConfigModeHelp(Configuration.getHelpType());
			} else {
				ConfigModes.printConfigModeHelp(ModeType.ROOT);
			}
			System.exit(0);
		}

		// From this point we want a header displayed
		Formatter.printHeader();

		// Display version information
		if (Configuration.getMode() == ModeType.VERSION) {
			System.exit(0);
		}

		if (!Configuration.hasUsername() || !Configuration.hasPassword() || !Configuration.hasServer()) {
			Formatter.usageError("No credentials were set, or server uri was missing.", ModeType.ROOT);
		}

		vCloudClient client = new vCloudClient();
		client.login(Configuration.getServer(), Configuration.getUsername(), Configuration.getPassword());

		if (Configuration.getMode() == ModeType.LIST) {
			if (Configuration.getListType() == null) {
				Formatter.usageError("Invalid list type was selected.", ModeType.LIST);
			}
			Formatter.printBorderedInfo(Configuration.dumpToString(ModeType.LIST));

			switch (Configuration.getListType()) {
			case ORG:
				client.listOrganizations();
				break;
			case VDC:
				client.listVDCs(Configuration.getOrganization());
				break;
			case CATALOG:
				client.listCatalogs(Configuration.getOrganization());
				break;
			case VAPP:
				client.listVApps(Configuration.getOrganization(), Configuration.getVDC());
				break;
			case VM:
				client.listVMs(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp());
				break;
			default:
				Formatter.printErrorLine("Not yet implemented");
				break;
			}
		} else if (Configuration.getMode() == ModeType.ADDVM) {
			Configuration.load(ModeType.ADDVM, args);
			Formatter.printBorderedInfo(Configuration.dumpToString(ModeType.ADDVM));

			Formatter.waitForTaskCompletion(client.addVM(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getCatalog(), Configuration.getTemplate(), Configuration.getFqdn(), Configuration.getDescription(), Configuration.getIp().getHostAddress(), Configuration.getNetwork()));
			Formatter.waitForTaskCompletion(client.setGuestCustomizations(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getFqdn(), false));
		} else if (Configuration.getMode() == ModeType.REMOVEVM || Configuration.getMode() == ModeType.POWERONVM || Configuration.getMode() == ModeType.POWEROFFVM || Configuration.getMode() == ModeType.SHUTDOWNVM || Configuration.getMode() == ModeType.CONSOLIDATEVM) {
			Configuration.load(Configuration.getMode(), args);
			Formatter.printBorderedInfo(Configuration.dumpToString(Configuration.getMode()));

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
			Configuration.load(ModeType.RESIZEDISK, args);
			Formatter.printBorderedInfo(Configuration.dumpToString(ModeType.RESIZEDISK));

			Formatter.waitForTaskCompletion(client.resizeVMDisks(Configuration.getOrganization(), Configuration.getVDC(), Configuration.getVApp(), Configuration.getVM(), Configuration.getDiskName(), Configuration.getDiskSize()));
		} else {
			Formatter.usageError("No mode was selected", ModeType.ROOT);
		}
	}
}
