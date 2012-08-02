package nl.cyso.vcloud.client;

import java.util.HashMap;
import java.util.logging.Level;

import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.constants.Version;

public class vCloudClient {

	private VcloudClient vcc;

	protected VcloudClient getVcc() {
		return vcc;
	}

	public void login(String uri, String username, String password) {
		VcloudClient.setLogLevel(Level.OFF);
		this.vcc = new VcloudClient(uri, Version.V1_5);
		try {
			this.vcc.registerScheme("https", 443, FakeSSLSocketFactory.getInstance());
		} catch (Exception e) {
			System.err.println("Unexpected error");
			System.err.println(e.getStackTrace());
			System.exit(1);
		}

		HashMap<String, ReferenceType> organizationsMap = null;
		try {
			this.vcc.login(username, password);
			organizationsMap = this.vcc.getOrgRefsByName();
		} catch (VCloudException ve) {
			System.err.println("An error occurred while logging in:\n\n");
			System.err.println(ve.getLocalizedMessage());
			System.exit(1);
		}

		if (organizationsMap.isEmpty()) {
			System.err.println("Invalid login for user " + username);
			System.exit(1);
		}
	}
}
