package nl.cyso.vcloud.client;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class Entry {

	public static void main(String[] args) {
		Options opt = new Options();

		// Connection options
		opt.addOption("u", "username", true, "vCloud Director username");
		opt.addOption("p", "password", true, "vCloud Director password");
		opt.addOption("s", "server", true, "vCloud Director server URI");

		// Modes
		opt.addOption("h", "help", false, "Show help");
		opt.addOption("l", "list", true, "List vCloud objects (org|vdc|vapp|catalog|vm)");

		CommandLine cli = null;
		try {
			cli = new PosixParser().parse(opt, args);
		} catch (ParseException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		if (cli.hasOption("help")) {
			new HelpFormatter().printHelp("vcloud-client", opt, true);
			System.exit(0);
		}

		if (!cli.hasOption("username") || !cli.hasOption("password") || !cli.hasOption("server")) {
			System.out.println("No credentials were set, or server uri was missing\n");
			new HelpFormatter().printHelp("vcloud-client", opt, true);
			System.exit(-1);
		}

		vCloudClient client = new vCloudClient();
		client.login(cli.getOptionValue("server"), cli.getOptionValue("username"), cli.getOptionValue("password"));
	}
}
