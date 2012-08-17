package nl.cyso.vcloud.client.config;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import nl.cyso.vcloud.client.Formatter;
import nl.cyso.vcloud.client.types.ListType;
import nl.cyso.vcloud.client.types.ModeType;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Configuration {

	private static final String[] connection = new String[] { "server", "username", "password" };
	private static final String[] selectors = new String[] { "organization", "vdc", "vapp", "vm", "catalog" };
	private static final String[] input = new String[] { "fqdn", "description", "template", "network", "ip", "disk-name", "disk-size" };

	private static Map<String, Object> configuration = new HashMap<String, Object>();

	protected static boolean has(String key) {
		return Configuration.configuration.containsKey(key);
	}

	protected static void set(String key, Object value) {
		Configuration.configuration.put(key, value);
	}

	protected static Object valueOrNull(String key) {
		if (Configuration.configuration.containsKey(key)) {
			return Configuration.configuration.get(key);
		} else {
			return null;
		}
	}

	public static boolean hasUsername() {
		return Configuration.has("username");
	}

	public static String getUsername() {
		return (String) Configuration.valueOrNull("username");
	}

	public static void setUsername(String username) {
		Configuration.set("username", username);
	}

	public static boolean hasPassword() {
		return Configuration.has("password");
	}

	public static String getPassword() {
		return (String) Configuration.valueOrNull("password");
	}

	public static void setPassword(String password) {
		Configuration.set("password", password);
	}

	public static boolean hasServer() {
		return Configuration.has("server");
	}

	public static String getServer() {
		return (String) Configuration.valueOrNull("server");
	}

	public static void setServer(String server) {
		Configuration.set("server", server);
	}

	public static boolean hasMode() {
		return Configuration.has("mode");
	}

	public static ModeType getMode() {
		return (ModeType) Configuration.valueOrNull("mode");
	}

	public static void setMode(ModeType mode) {
		Configuration.set("mode", mode);
	}

	public static boolean hasVDC() {
		return Configuration.has("vdc");
	}

	public static String getVDC() {
		return (String) Configuration.valueOrNull("vdc");
	}

	public static void setVDC(String vdc) {
		Configuration.set("vdc", vdc);
	}

	public static boolean hasVApp() {
		return Configuration.has("vapp");
	}

	public static String getVApp() {
		return (String) Configuration.valueOrNull("vapp");
	}

	public static void setVApp(String vapp) {
		Configuration.set("vapp", vapp);
	}

	public static boolean hasVM() {
		return Configuration.has("vm");
	}

	public static String getVM() {
		return (String) Configuration.valueOrNull("vm");
	}

	public static void setVM(String vm) {
		Configuration.set("vm", vm);
	}

	public static boolean hasCatalog() {
		return Configuration.has("catalog");
	}

	public static String getCatalog() {
		return (String) Configuration.valueOrNull("catalog");
	}

	public static void setCatalog(String catalog) {
		Configuration.set("catalog", catalog);
	}

	public static boolean hasOrganization() {
		return Configuration.has("organization");
	}

	public static String getOrganization() {
		return (String) Configuration.valueOrNull("organization");
	}

	public static void setOrganization(String organization) {
		Configuration.set("organization", organization);
	}

	public static boolean hasListType() {
		return Configuration.has("listType");
	}

	public static ListType getListType() {
		return (ListType) Configuration.valueOrNull("listType");
	}

	public static void setListType(ListType listType) {
		Configuration.set("listType", listType);
	}

	public static boolean hasHelpType() {
		return Configuration.has("help-type");
	}

	public static ModeType getHelpType() {
		return (ModeType) Configuration.valueOrNull("help-type");
	}

	public static void setHelpType(ModeType mode) {
		Configuration.set("help-type", mode);
	}

	public static boolean hasFqdn() {
		return Configuration.has("fqdn");
	}

	public static String getFqdn() {
		return (String) Configuration.valueOrNull("fqdn");
	}

	public static void setFqdn(String fqdn) {
		Configuration.set("fqdn", fqdn);
	}

	public static boolean hasDescription() {
		return Configuration.has("description");
	}

	public static String getDescription() {
		return (String) Configuration.valueOrNull("description");
	}

	public static void setDescription(String description) {
		Configuration.set("description", description);
	}

	public static boolean hasTemplate() {
		return Configuration.has("template");
	}

	public static String getTemplate() {
		return (String) Configuration.valueOrNull("template");
	}

	public static void setTemplate(String template) {
		Configuration.set("template", template);
	}

	public static boolean hasIp() {
		return Configuration.has("ip");
	}

	public static InetAddress getIp() {
		return (InetAddress) Configuration.valueOrNull("ip");
	}

	public static void setIp(InetAddress ip) {
		Configuration.set("ip", ip);
	}

	public static void setIp(String ip) throws UnknownHostException {
		Configuration.set("ip", InetAddress.getByName(ip));
	}

	public static boolean hasNetwork() {
		return Configuration.has("network");
	}

	public static String getNetwork() {
		return (String) Configuration.valueOrNull("network");
	}

	public static void setNetwork(String network) {
		Configuration.set("network", network);
	}

	public static boolean hasDiskName() {
		return Configuration.has("disk-name");
	}

	public static String getDiskName() {
		return (String) Configuration.valueOrNull("disk-name");
	}

	public static void setDiskName(String diskname) {
		Configuration.set("disk-name", diskname);
	}

	public static boolean hasDiskSize() {
		return Configuration.has("disk-size");
	}

	public static BigInteger getDiskSize() {
		return (BigInteger) Configuration.valueOrNull("disk-size");
	}

	public static void setDiskSize(BigInteger disksize) {
		Configuration.set("disk-size", disksize);
	}

	public static void setDiskSize(String disksize) {
		Configuration.set("disk-size", new BigInteger(disksize));
	}

	public static CommandLine parseCli(ConfigMode opt, String[] args) {
		CommandLine cli = null;
		try {
			cli = new IgnorePosixParser(true).parse(opt, args);
		} catch (MissingArgumentException me) {
			Formatter.usageError(me.getLocalizedMessage(), opt);
			System.exit(-1);
		} catch (MissingOptionException mo) {
			Formatter.usageError(mo.getLocalizedMessage(), opt);
			System.exit(-1);
		} catch (AlreadySelectedException ase) {
			Formatter.usageError(ase.getLocalizedMessage(), opt);
		} catch (UnrecognizedOptionException uoe) {
			Formatter.usageError(uoe.getLocalizedMessage(), opt);
		} catch (ParseException e) {
			Formatter.printStackTrace(e);
			System.exit(-1);
		}

		return cli;
	}

	public static void load(ConfigMode mode, String[] args) {
		CommandLine cli = Configuration.parseCli(mode, args);
		Configuration.load(cli);
	}

	public static void load(CommandLine cli) {
		for (Option opt : cli.getOptions()) {
			if (cli.hasOption(opt.getLongOpt())) {
				if (opt.getLongOpt().equals("help")) {
					Configuration.setMode(ModeType.HELP);
					if (cli.getOptionValue(opt.getLongOpt()) != null) {
						Configuration.setHelpType(ModeType.valueOf(cli.getOptionValue(opt.getLongOpt()).toUpperCase()));
					}
				} else if (opt.getLongOpt().equals("version")) {
					Configuration.setMode(ModeType.VERSION);
				} else if (opt.getLongOpt().equals("list")) {
					Configuration.setMode(ModeType.LIST);
					Configuration.setListType(ListType.valueOf(cli.getOptionValue(opt.getLongOpt()).toUpperCase()));
				} else if (opt.getLongOpt().equals("add-vm")) {
					Configuration.setMode(ModeType.ADDVM);
				} else if (opt.getLongOpt().equals("remove-vm")) {
					Configuration.setMode(ModeType.REMOVEVM);
				} else if (opt.getLongOpt().equals("poweron-vm")) {
					Configuration.setMode(ModeType.POWERONVM);
				} else if (opt.getLongOpt().equals("poweroff-vm")) {
					Configuration.setMode(ModeType.POWEROFFVM);
				} else if (opt.getLongOpt().equals("shutdown-vm")) {
					Configuration.setMode(ModeType.SHUTDOWNVM);
				} else if (opt.getLongOpt().equals("resize-disk")) {
					Configuration.setMode(ModeType.RESIZEDISK);
				} else if (opt.getLongOpt().equals("consolidate-vm")) {
					Configuration.setMode(ModeType.CONSOLIDATEVM);
				} else if (opt.getLongOpt().equals("ip")) {
					try {
						Configuration.setIp(cli.getOptionValue(opt.getLongOpt()));
					} catch (UnknownHostException uhe) {
						Configuration.setIp((InetAddress) null);
					}
				} else if (opt.getLongOpt().equals("disk-size")) {
					Configuration.setDiskSize(cli.getOptionValue(opt.getLongOpt()));
				} else {
					Configuration.set(opt.getLongOpt(), cli.getOptionValue(opt.getLongOpt()));
				}
			}
		}
	}

	public static void loadFile(String filename) {
		org.apache.commons.configuration.Configuration conf = null;
		try {
			conf = new PropertiesConfiguration(filename);
		} catch (ConfigurationException e) {
			Formatter.printErrorLine("Failed to load configuration file");
			Formatter.printErrorLine(e.getLocalizedMessage());
			return;
		}

		Iterator<String> i = conf.getKeys();
		while (i.hasNext()) {
			String key = i.next();

			if (key.equals("help")) {
				Configuration.setMode(ModeType.HELP);
			} else if (key.equals("version")) {
				Configuration.setMode(ModeType.VERSION);
			} else if (key.equals("list")) {
				Configuration.setMode(ModeType.LIST);
				Configuration.setListType(ListType.valueOf(conf.getString(key).toUpperCase()));
			} else if (key.equals("ip")) {
				try {
					Configuration.setIp(conf.getString(key));
				} catch (UnknownHostException uhe) {
					Configuration.setIp((InetAddress) null);
				}
			} else {
				Configuration.set(key, conf.getString(key));
			}
		}
	}

	public String toString() {
		return Configuration.dumpToString();
	}

	public static String dumpToString() {
		return Configuration.dumpToString(ConfigModes.getConsolidatedModes());
	}

	public static String dumpToString(ConfigMode mode) {
		StringBuilder dump = new StringBuilder();

		dump.append(String.format("Selected operation: %s\n", Configuration.getMode().toString()));
		if (Configuration.getMode() == ModeType.LIST) {
			dump.append(String.format("Selected mode: %s\n", Configuration.getListType().toString()));
		}

		dump.append("Connection configuration: \n");
		for (String in : Configuration.connection) {
			if (Configuration.has(in) && mode.hasOption(in)) {
				if (in.equals("password")) {
					continue;
				}
				dump.append(String.format("\t %s: %s\n", in, Configuration.valueOrNull(in)));
			}
		}

		dump.append("Selector configuration: \n");
		for (String in : Configuration.selectors) {
			if (Configuration.has(in) && mode.hasOption(in)) {
				dump.append(String.format("\t %s: %s\n", in, Configuration.valueOrNull(in)));
			}
		}

		dump.append("User input: \n");
		for (String in : Configuration.input) {
			if (Configuration.has(in) && mode.hasOption(in)) {
				if (in.equals("ip")) {
					dump.append(String.format("\t %s: %s\n", in, ((InetAddress) Configuration.valueOrNull(in)).getHostAddress()));
				} else {
					dump.append(String.format("\t %s: %s\n", in, Configuration.valueOrNull(in)));
				}
			}
		}

		return dump.toString();
	}
}
