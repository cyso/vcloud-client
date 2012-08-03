package nl.cyso.vcloud.client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Entry {
	public static final String name = "vcloud-client";

	public static void usageError(String error, Options opts) {
		System.out.println(error + "\n");
		new HelpFormatter().printHelp(name, opts, true);
		System.exit(-1);
	}

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		Options opt = new Options();

		// Connection options
		opt.addOption("u", "username", true, "vCloud Director username");
		opt.addOption("p", "password", true, "vCloud Director password");
		opt.addOption("s", "server", true, "vCloud Director server URI");

		// Modes
		opt.addOption("h", "help", false, "Show help");
		opt.addOption("l", "list", true, "List vCloud objects (org|vdc|vapp|catalog|vm)");
		opt.addOption("a", "add-vm", false, "Add a new VM from a vApp Template to an existing vApp");

		// Selectors
		opt.addOption(OptionBuilder.withLongOpt("org").hasArg().withArgName("ORG").withDescription("Select this Organization").create());
		opt.addOption(OptionBuilder.withLongOpt("vdc").hasArg().withArgName("VDC").withDescription("Select this Virtual Data Center").create());
		opt.addOption(OptionBuilder.withLongOpt("vapp").hasArg().withArgName("VAPP").withDescription("Select this vApp").create());
		opt.addOption(OptionBuilder.withLongOpt("vm").hasArg().withArgName("VM").withDescription("Select this VM").create());
		opt.addOption(OptionBuilder.withLongOpt("catalog").hasArg().withArgName("CATALOG").withDescription("Select this Catalog").create());

		// User input
		opt.addOption("f", "fqdn", true, "Name of object to create");
		opt.addOption("d", "description", true, "Description of object to create");
		opt.addOption("t", "template", true, "Template of object to create");
		opt.addOption("i", "ip", true, "IP of the object to create");
		opt.addOption("n", "network", true, "Network of the object to create");

		CommandLine cli = null;
		try {
			cli = new PosixParser().parse(opt, args);
		} catch (MissingArgumentException me) {
			usageError(me.getLocalizedMessage(), opt);
			System.exit(-1);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (cli.hasOption("help")) {
			new HelpFormatter().printHelp("vcloud-client", opt, true);
			System.exit(0);
		}

		if (!cli.hasOption("username") || !cli.hasOption("password") || !cli.hasOption("server")) {
			usageError("No credentials were set, or server uri was missing.", opt);
		}

		vCloudClient client = new vCloudClient();
		client.login(cli.getOptionValue("server"), cli.getOptionValue("username"), cli.getOptionValue("password"));

		if (cli.hasOption("list")) {
			ListType type = null;
			try {
				type = ListType.valueOf(cli.getOptionValue("list").toUpperCase());
			} catch (IllegalArgumentException iae) {
				usageError("Invalid list type was selected.", opt);
			}

			switch (type) {
			case ORG:
				client.listOrganizations();
				break;
			case VDC:
				if (!cli.hasOption("org")) {
					usageError("An organization must also be specified when listing VDCs", opt);
				}
				client.listVDCs(cli.getOptionValue("org"));
				break;
			case CATALOG:
				if (!cli.hasOption("org")) {
					usageError("An organization must also be specified when listing Catalogs", opt);
				}
				client.listCatalogs(cli.getOptionValue("org"));
				break;
			case VAPP:
				if (!cli.hasOption("org")) {
					usageError("An organization must also be specified when listing vApps", opt);
				}
				if (!cli.hasOption("vdc")) {
					usageError("A VDC must also be specified when listing vApps", opt);
				}
				client.listVApps(cli.getOptionValue("org"), cli.getOptionValue("vdc"));
				break;
			default:
				System.err.println("Not yet implemented");
				break;
			}
		} else if (cli.hasOption("add-vm")) {
			if (!cli.hasOption("org")) {
				usageError("An existing organization has to be selected", opt);
			}
			if (!cli.hasOption("vdc")) {
				usageError("An existing virtual data center has to be selected", opt);
			}
			if (!cli.hasOption("vapp")) {
				usageError("An existing vApp has to be selected", opt);
			}
			if (!cli.hasOption("catalog")) {
				usageError("An existing Catalog has to be selected", opt);
			}
			if (!cli.hasOption("fqdn")) {
				usageError("A FQDN has to be specified for the new VM", opt);
			}
			if (!cli.hasOption("description")) {
				usageError("A description has to be specified for the new VM", opt);
			}
			if (!cli.hasOption("template")) {
				usageError("A template has to be specified for the new VM", opt);
			}
			if (!cli.hasOption("ip")) {
				usageError("An IP has to be specified for the new VM", opt);
			}
			if (!cli.hasOption("network")) {
				usageError("A Network has to be specified for the new VM", opt);
			}

			client.recomposeVApp(cli.getOptionValue("org"), cli.getOptionValue("vdc"), cli.getOptionValue("vapp"), cli.getOptionValue("catalog"), cli.getOptionValue("template"), cli.getOptionValue("fqdn"), cli.getOptionValue("description"), cli.getOptionValue("ip"), cli.getOptionValue("network"));
		}

		// Something went wrong if we got here
		System.exit(-1);
	}
}
