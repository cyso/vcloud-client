package nl.cyso.vcloud.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;

import com.vmware.vcloud.api.rest.schema.ReferenceType;
import com.vmware.vcloud.sdk.Organization;
import com.vmware.vcloud.sdk.VCloudException;
import com.vmware.vcloud.sdk.Vapp;
import com.vmware.vcloud.sdk.VappTemplate;
import com.vmware.vcloud.sdk.VcloudClient;
import com.vmware.vcloud.sdk.Vdc;
import com.vmware.vcloud.sdk.constants.Version;

public class vCloudClient {

	private VcloudClient vcc = null;

	protected VcloudClient getVcc() {
		return vcc;
	}

	private void vccPreCheck() {
		if (this.vcc == null) {
			throw new IllegalStateException("vCloudClient was not logged in");
		}
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

	public void listOrganizations() {
		this.vccPreCheck();

		try {
			Collection<ReferenceType> orgs = this.vcc.getOrgRefs();

			System.out.println("Organizations:\n");
			for (ReferenceType org : orgs) {
				System.out.println(String.format("%s", org.getName()));
			}
		} catch (VCloudException e) {
			System.err.println("An error occured while retrieving organizations");
			System.exit(1);
		}
	}

	public void listVDCs(String org) {
		this.vccPreCheck();

		try {
			System.out.println("Virtual Data Centers:\n");
			ReferenceType orgRef = this.vcc.getOrgRefsByName().get(org);
			for (ReferenceType vdcRef : Organization.getOrganizationByReference(this.vcc, orgRef).getVdcRefs()) {
				Vdc vdc = Vdc.getVdcByReference(this.vcc, vdcRef);
				System.out.println(String.format("%-20s - %s", vdcRef.getName(), vdc.getResource().getDescription()));
			}
		} catch (VCloudException e) {
			System.err.println("An error occured while retrieving virtual data centers");
			System.exit(1);
		}
	}

	public void listVApps(String org, String vdc) {
		this.vccPreCheck();

		Vdc vdcObj = this.getVDC(org, vdc);

		try {
			System.out.println("vApps:\n");
			for (ReferenceType vappRef : vdcObj.getVappRefs()) {
				Vapp vapp = Vapp.getVappByReference(this.vcc, vappRef);
				System.out.println(String.format("%-20s - %s", vappRef.getName(), vapp.getResource().getDescription()));
			}
		} catch (VCloudException e) {
			System.err.println("An error occured while retrieving vApps");
			System.exit(1);
		}
	}

	public void listVAppTemplates(String org, String vdc) {
		this.vccPreCheck();

		Vdc vdcObj = this.getVDC(org, vdc);

		try {
			System.out.println("vApp Templates:\n");
			for (ReferenceType vappTemplateRef : vdcObj.getVappTemplateRefs()) {
				VappTemplate vappTemplate = VappTemplate.getVappTemplateByReference(this.vcc, vappTemplateRef);
				System.out.println(String.format("----\nName: %s\n\n%s\n----\n", vappTemplateRef.getName(), vappTemplate.getResource().getDescription()));
			}
		} catch (VCloudException e) {
			System.err.println("An error occured while retrieving vApp Templates");
			System.exit(1);
		}
	}

	private Vdc getVDC(String org, String name) {
		this.vccPreCheck();

		Vdc vdcObj = null;
		try {
			Organization o = Organization.getOrganizationByReference(this.vcc, this.vcc.getOrgRefByName(org));
			ReferenceType vdcRef = o.getVdcRefByName(name);
			vdcObj = Vdc.getVdcByReference(this.vcc, vdcRef);
		} catch (VCloudException e) {
			System.err.println("An error occured while selecting the virtual data center");
			System.exit(1);
		} catch (NullPointerException ne) {
			System.err.println("Virtual data center does not exist");
			System.exit(1);
		}

		return vdcObj;
	}
}
