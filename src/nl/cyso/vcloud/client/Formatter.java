package nl.cyso.vcloud.client;

import java.util.concurrent.TimeoutException;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import com.vmware.vcloud.sdk.Task;
import com.vmware.vcloud.sdk.VCloudException;

public class Formatter {
	public static void usageError(String error, Options opts) {
		System.out.println(error + "\n");
		new HelpFormatter().printHelp(Version.PROJECT_NAME, opts, true);
		System.exit(-1);
	}

	// TODO: find something better than hard coding examples
	public static void printExamples() {
		System.out.println("\nexamples:");
		System.out.println(String.format(" %s -c cloud.cfg", Version.PROJECT_NAME));
		System.out.println("  Load configuration from file \"cloud.cfg\"\n");
		System.out.println(String.format(" %s -c cloud.cfg -l org", Version.PROJECT_NAME));
		System.out.println("  List all organizations that you have access to\n");
		System.out.println(String.format(" %s -c cloud.cfg --organization=\"sample-org\" -l vdc", Version.PROJECT_NAME));
		System.out.println("  List all virtual data centers of organization \"sample-org\" and their networks\n");
		System.out.println(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" -l vapp", Version.PROJECT_NAME));
		System.out.println("  List all vApps of virtual data center \"sample-vdc\" of organization \"sample-org\" and their networks\n");
		System.out.println(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" --vapp=\"sample-vapp\" -l vm", Version.PROJECT_NAME));
		System.out.println("  List all VMs in vApp \"sample-vapp\" of virtual data center \"sample-vdc\" of organization \"sample-org\"\n");
		System.out.println(String.format(" %s -c cloud.cfg --organization=\"sample-org\" -l catalog", Version.PROJECT_NAME));
		System.out.println("  List all available catalogs and their content of organization \"sample-org\"\n");
		System.out.println(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" --vapp=\"sample-vapp\" --catalog=\"sample-catalog\" --template=\"sample-template\" \\", Version.PROJECT_NAME));
		System.out.println("    --fqdn=\"new.vm.fqdn.com\" --description=\"This is a new VM\" --network=\"vcloud-netw-1\" --ip=\"1.2.3.4\" --add-vm");
		System.out.println("  Create a new VM in vApp \"sample-vapp\" of virtual data center \"sample-vdc\" of organization \"sample-org\",");
		System.out.println("     with FQDN \"new.vm.fqdn.com\", a description, and IP \"1.2.3.4\" in network \"vcloud-netw-1\"\n");
		System.out.println(String.format(" %s -c cloud.cfg --organization=\"sample-org\" --vdc=\"sample-vdc\" --vapp=\"sample-vapp\" --vm=\"existing.vm.fqdn.com\" --remove-vm", Version.PROJECT_NAME));
		System.out.println("  Remove an existing VM named \"existing.vm.fqdn.com\" from vApp \"sample-vapp\" of virtual data center \"sample-vdc\" of organization \"sample-org\".\n");
	}

	public static void waitForTaskCompletion(Task task) {
		String message = "Waiting for task completion ";
		String[] twirl = new String[] { "-", "\\", "|", "/" };
		boolean wait = true;
		int counter = 0;
		while (wait) {
			System.out.print("\r" + message + twirl[counter % twirl.length]);

			try {
				task.waitForTask(20);
			} catch (TimeoutException e) {
				// Still waiting...
				counter++;
				continue;
			} catch (VCloudException vce) {
				System.out.print("\n");
				System.err.println("An error occured while executing task");
				System.err.println(vce.getLocalizedMessage());
				System.exit(-1);
			}

			System.out.print("\n");
			System.out.println("Done");
			wait = false;
		}
	}
}
