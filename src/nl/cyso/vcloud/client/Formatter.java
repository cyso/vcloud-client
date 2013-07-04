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

import java.util.concurrent.TimeoutException;

import nl.cyso.vcloud.client.config.ConfigModes;
import nl.cyso.vcloud.client.types.ModeType;

import org.apache.commons.lang.StringUtils;

import com.vmware.vcloud.sdk.Task;
import com.vmware.vcloud.sdk.VCloudException;

public class Formatter {
	public static void usageError(String error, ModeType mode) {
		Formatter.printErrorLine(error + "\n");
		ConfigModes.printConfigModeHelp(mode);
		System.exit(-1);
	}

	// TODO: find something better than hard coding examples
	public static void printExamples() {
		Formatter.printInfoLine("\nexamples:");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg", Version.PROJECT_NAME));
		Formatter.printInfoLine("  Load configuration from file \"cloud.cfg\"\n");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg -l org", Version.PROJECT_NAME));
		Formatter.printInfoLine("  List all organizations that you have access to\n");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg --organization=\"sample-org\" -l vdc", Version.PROJECT_NAME));
		Formatter.printInfoLine("  List all virtual data centers of organization \"sample-org\" and their networks\n");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" -l vapp", Version.PROJECT_NAME));
		Formatter.printInfoLine("  List all vApps of virtual data center \"sample-vdc\" of organization \"sample-org\" and their networks\n");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" --vapp=\"sample-vapp\" -l vm", Version.PROJECT_NAME));
		Formatter.printInfoLine("  Formatter.printInfopp \"sample-vapp\" of virtual data center \"sample-vdc\" of organization \"sample-org\"\n");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg --organization=\"sample-org\" -l catalog", Version.PROJECT_NAME));
		Formatter.printInfoLine("  List all available catalogs and their content of organization \"sample-org\"\n");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" --vapp=\"sample-vapp\" --catalog=\"sample-catalog\" --template=\"sample-template\" \\", Version.PROJECT_NAME));
		Formatter.printInfoLine("    --fqdn=\"new.vm.fqdn.com\" --description=\"This is a new VM\" --network=\"vcloud-netw-1\" --ip=\"1.2.3.4\" --add-vm");
		Formatter.printInfoLine("  Create a new VM in vApp \"sample-vapp\" of virtual data center \"sample-vdc\" of organization \"sample-org\",");
		Formatter.printInfoLine("     with FQDN \"new.vm.fqdn.com\", a description, and IP \"1.2.3.4\" in network \"vcloud-netw-1\"\n");
		Formatter.printInfoLine(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" --vapp=\"sample-vapp\" --vm=\"existing.vm.fqdn.com\" --remove-vm", Version.PROJECT_NAME));
		Formatter.printInfoLine("  Remove an existing VM named \"existing.vm.fqdn.com\" from vApp \"sample-vapp\" of virtual data center \"sample-vdc\" of organization \"sample-org\".\n");
	}

	public static String getHeader() {
		StringBuilder header = new StringBuilder();

		header.append(String.format("%s version %s\n", Version.PROJECT_NAME, Version.RELEASE_VERSION));
		header.append(String.format("BUILD_VERSION: %s\n", Version.BUILD_VERSION));

		return header.toString();
	}

	public static void printHeader() {
		Formatter.printBorderedInfo(Formatter.getHeader());
	}

	public static void waitForTaskCompletion(Task task) {
		String message = "Waiting for task completion ";
		String[] twirl = new String[] { "-", "\\", "|", "/" };
		boolean wait = true;
		int counter = 0;
		while (wait) {
			Formatter.printInfo("\r" + message + twirl[counter % twirl.length]);

			try {
				task.waitForTask(20);
			} catch (TimeoutException e) {
				// Still waiting...
				counter++;
				continue;
			} catch (VCloudException vce) {
				Formatter.printInfo("\n");
				Formatter.printErrorLine("An error occured while executing task");
				Formatter.printErrorLine(vce.getLocalizedMessage());
				System.exit(-1);
			}

			Formatter.printInfo("\n");
			Formatter.printInfoLine("Done");
			wait = false;
		}
	}

	public static void printError(Object error) {
		System.err.print(error);
	}

	public static void printInfo(Object info) {
		System.out.print(info);
	}

	public static void printErrorLine(Object error) {
		System.err.println(error);
	}

	public static void printInfoLine(Object info) {
		System.out.println(info);
	}

	public static void printBorderedInfo(Object info) {
		Formatter.printBordered(info, false);
	}

	public static void printBorderedError(Object error) {
		Formatter.printBordered(error, true);
	}

	private static void printBordered(Object msg, boolean error) {
		String[] lines = msg.toString().split("\n");
		int longest = 0;
		for (String line : lines) {
			if (line.length() > longest) {
				longest = line.length();
			}
		}
		if (error) {
			Formatter.printErrorLine(StringUtils.repeat("-", longest));
			Formatter.printError(msg);
			Formatter.printErrorLine(StringUtils.repeat("-", longest));
		} else {
			Formatter.printInfoLine(StringUtils.repeat("-", longest));
			Formatter.printInfo(msg);
			Formatter.printInfoLine(StringUtils.repeat("-", longest));
		}
	}

	public static void printStackTrace(Throwable e) {
		e.printStackTrace(System.err);
	}
}
