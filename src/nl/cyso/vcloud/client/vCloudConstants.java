package nl.cyso.vcloud.client;

public class vCloudConstants {
	public static final String GLOBAL_SDK_LOGGER_NAME = "com.vmware.vcloud.sdk";
	public static final String API_URI_PREFIX = "api";
	public static final String ACCEPT_HEADER_NAME = "Accept";
	public static final String LOCATION_HEADER = "Location";
	public static final String VCLOUD_AUTHORIZATION_HEADER = "x-vcloud-authorization";
	public static final String UTF_8_ENCODING = "UTF-8";
	public static final String CONTENT_RANGE_HEADER = "Content-Range";
	public static final String CONTENT_RANGE_BYTES_HEADER = "bytes ";

	public static class ActionUri {
		public static final String UPLOAD_VAPP_TEMPLATE = "/action/uploadVAppTemplate";
		public static final String INSTANTIATE_OVF = "/action/instantiateOvf";
		public static final String VDC_MEDIA = "/media";
		public static final String CLONE_MEDIA = "/action/cloneMedia";
		public static final String MOVE_MEDIA = "/action/moveMedia";
		public static final String CLONE_VAPP_TEMPLATE = "/action/cloneVAppTemplate";
		public static final String MOVE_VAPP_TEMPLATE = "/action/moveVAppTemplate";
		public static final String INSTANTIATE_VAPP_TEMPLATE = "/action/instantiateVAppTemplate";
		public static final String CLONE_VAPP = "/action/cloneVApp";
		public static final String MOVE_VAPP = "/action/moveVApp";
		public static final String CAPTURE_VAPP = "/action/captureVApp";
		public static final String COMPOSE_VAPP = "/action/composeVApp";
		public static final String VAPP_DISCARD_STATE = "/action/discardSuspendedState";
		public static final String ENABLE_DOWNLOAD = "/action/enableDownload";
		public static final String DISABLE_DOWNLOAD = "/action/disableDownload";
		public static final String VAPP_DEPLOY = "/action/deploy";
		public static final String VAPP_UNDEPLOY = "/action/undeploy";
		public static final String VAPP_UNDEPLOY_EXTENDED = "/action/undeployExtended";
		public static final String VAPP_POWER_ON = "/power/action/powerOn";
		public static final String VAPP_POWER_OFF = "/power/action/powerOff";
		public static final String VAPP_RESET = "/power/action/reset";
		public static final String VAPP_SUSPEND = "/power/action/suspend";
		public static final String VAPP_SHUTDOWN = "/power/action/shutdown";
		public static final String VAPP_REBOOT = "/power/action/reboot";
		public static final String VAPP_SCREEN = "/screen";
		public static final String VAPP_ACQUIRE_TICKET = "/screen/action/acquireTicket";
		public static final String VM_INSERT_MEDIA = "/media/action/insertMedia";
		public static final String VM_EJECT_MEDIA = "/media/action/ejectMedia";
		public static final String VAPP_QUESTION_ANSWER = "/question/action/answer";
		public static final String RECOMPOSE_VAPP = "/action/recomposeVApp";
		public static final String INSTALL_VMWARETOOLS = "/action/installVMwareTools";
		public static final String UPGRADE_HARDWARE = "/action/upgradeHardwareVersion";
		public static final String CONSOLIDATE = "/action/consolidate";
		public static final String RELOCATE_VM = "/action/relocate";
		public static final String ENABLE_MAINTENANCE_MODE = "/action/enterMaintenanceMode";
		public static final String DISABLE_MAINTENANCE_MODE = "/action/exitMaintenanceMode";
		public static final String VAPP_NETWORK_RESET = "/action/reset";
		public static final String OVF = "/ovf";
		public static final String CONTROL_ACCESS = "/action/controlAccess";
		public static final String CONTROL_ACCESS_VIEW = "/controlAccess/";
		public static final String CANCEL_TASK = "/action/cancel";
	}

	public static class MediaType {
		public static final String ORGANIZATION = "application/vnd.vmware.vcloud.org+xml";
		public static final String ORGANIZATION_LIST = "application/vnd.vmware.vcloud.orgList+xml";
		public static final String VDC = "application/vnd.vmware.vcloud.vdc+xml";
		public static final String UPLOAD_VAPP_TEMPLATE_PARAMS = "application/vnd.vmware.vcloud.uploadVAppTemplateParams+xml";
		public static final String INSTANTIATE_OVF_PARAMS = "application/vnd.vmware.vcloud.instantiateOvfParams+xml";
		public static final String INSTANTIATE_VAPP_TEMPLATE_PARAMS = "application/vnd.vmware.vcloud.instantiateVAppTemplateParams+xml";
		public static final String CLONE_VAPP_PARAMS = "application/vnd.vmware.vcloud.cloneVAppParams+xml";
		public static final String MOVE_VAPP_PARAMS = "application/vnd.vmware.vcloud.moveVAppParams+xml";
		public static final String CLONE_VAPP_TEMPLATE_PARAMS = "application/vnd.vmware.vcloud.cloneVAppTemplateParams+xml";
		public static final String MOVE_VAPP_TEMPLATE_PARAMS = "application/vnd.vmware.vcloud.moveVAppTemplateParams+xml";
		public static final String CLONE_MEDIA_PARAMS = "application/vnd.vmware.vcloud.cloneMediaParams+xml";
		public static final String MOVE_MEDIA_PARAMS = "application/vnd.vmware.vcloud.moveMediaParams+xml";
		public static final String DEPLOY_VAPP_PARAMS = "application/vnd.vmware.vcloud.deployVAppParams+xml";
		public static final String UNDEPLOY_VAPP_PARAMS = "application/vnd.vmware.vcloud.undeployVAppParams+xml";
		public static final String UNDEPLOY_VAPP_PARAMS_EXTENDED = "application/vnd.vmware.vcloud.undeployVAppParamsExtended+xml";
		public static final String CAPTURE_VAPP_PARAMS = "application/vnd.vmware.vcloud.captureVAppParams+xml";
		public static final String COMPOSE_VAPP_PARAMS = "application/vnd.vmware.vcloud.composeVAppParams+xml";
		public static final String VAPP_TEMPLATE = "application/vnd.vmware.vcloud.vAppTemplate+xml";
		public static final String VAPP = "application/vnd.vmware.vcloud.vApp+xml";
		public static final String VAPP_NETWORK = "application/vnd.vmware.vcloud.vAppNetwork+xml";
		public static final String VM = "application/vnd.vmware.vcloud.vm+xml";
		public static final String MEDIA = "application/vnd.vmware.vcloud.media+xml";
		public static final String NETWORK = "application/vnd.vmware.vcloud.network+xml";
		public static final String ORG_NETWORK = "application/vnd.vmware.vcloud.orgNetwork+xml";
		public static final String TASK = "application/vnd.vmware.vcloud.task+xml";
		public static final String TASKS_LIST = "application/vnd.vmware.vcloud.tasksList+xml";
		public static final String CATALOG = "application/vnd.vmware.vcloud.catalog+xml";
		public static final String CATALOG_ITEM = "application/vnd.vmware.vcloud.catalogItem+xml";
		public static final String ERROR = "application/vnd.vmware.vcloud.error+xml";
		public static final String SCREEN_TICKET = "application/vnd.vmware.vcloud.screenTicket+xml";
		public static final String RELOCATE_VM = "application/vnd.vmware.vcloud.relocateVmParams+xml";
		public static final String CONTROL_ACCESS = "application/vnd.vmware.vcloud.controlAccess+xml";
		public static final String MEDIA_INSERT_EJECT_PARAMS = "application/vnd.vmware.vcloud.mediaInsertOrEjectParams+xml";
		public static final String QUESTION = "application/vnd.vmware.vcloud.vmPendingQuestion+xml";
		public static final String ANSWER = "application/vnd.vmware.vcloud.vmPendingAnswer+xml";
		public static final String RECOMPOSE_VAPP_PARAMS = "application/vnd.vmware.vcloud.recomposeVAppParams+xml";
		public static final String OWNER = "application/vnd.vmware.vcloud.owner+xml";
		public static final String BLOCKING_TASK = "application/vnd.vmware.admin.blockingTask+xml";
		public static final String METADATA = "application/vnd.vmware.vcloud.metadata+xml";
		public static final String METADATA_VALUE = "application/vnd.vmware.vcloud.metadata.value+xml";
		public static final String LEASE_SETTINGS_SECTION = "application/vnd.vmware.vcloud.leaseSettingsSection+xml";
		public static final String STARTUP_SECTION = "application/vnd.vmware.vcloud.startupSection+xml";
		public static final String NETWORK_SECTION = "application/vnd.vmware.vcloud.networkSection+xml";
		public static final String NETWORK_CONNECTION_SECTION = "application/vnd.vmware.vcloud.networkConnectionSection+xml";
		public static final String PRODUCT_SECTIONS = "application/vnd.vmware.vcloud.productSections+xml";
		public static final String NETWORK_CONFIG_SECTION = "application/vnd.vmware.vcloud.networkConfigSection+xml";
		public static final String VIRTUAL_HARDWARE_SECTION = "application/vnd.vmware.vcloud.virtualHardwareSection+xml";
		public static final String OPERATING_SYSTEM_SECTION = "application/vnd.vmware.vcloud.operatingSystemSection+xml";
		public static final String GUEST_CUSTOMIZATION_SECTION = "application/vnd.vmware.vcloud.guestCustomizationSection+xml";
		public static final String CUSTOMIZATION_SECTION = "application/vnd.vmware.vcloud.customizationSection+xml";
		public static final String RASD_ITEM = "application/vnd.vmware.vcloud.rasdItem+xml";
		public static final String RASD_ITEMS_LIST = "application/vnd.vmware.vcloud.rasdItemsList+xml";
		public static final String OVF = "text/xml";
		public static final String QUERY_ALTERNATE_REFERENCES = "application/vnd.vmware.vcloud.query.references+xml";
		public static final String QUERY_ALTERNATE_ID_RECORDS = "application/vnd.vmware.vcloud.query.idrecords+xml";
		public static final String QUERY_ALTERNATE_RECORDS = "application/vnd.vmware.vcloud.query.records+xml";
	}

	public static class RelationType {
		public static final String UP = "up";
		public static final String CATALOG_ITEM = "catalogItem";
		public static final String CONTROL_ACCESS = "controlAccess";
		public static final String DOWN = "down";
		public static final String REPAIR = "repair";
		public static final String SYNC_SYSLOG_SETTINGS = "syncSyslogSettings";
		public static final String UPLOAD_DEFAULT = "upload:default";
		public static final String DOWNLOAD_DEFAULT = "download:default";
		public static final String NEXT_PAGE = "nextPage";
		public static final String ALTERNATE = "alternate";
		public static final String LAST_PAGE = "lastPage";
		public static final String PREVIOUS_PAGE = "previousPage";
		public static final String FIRST_PAGE = "firstPage";
	}

	public static class Uri {
		public static final String API_VERSIONS = "/versions";
		public static final String SESSIONS = "/sessions";
		public static final String SESSION = "/session";
		public static final String ENTITY = "/entity";
		public static final String SCHEMA = "/schema/";
		public static final String ORGANIZATION = "/org/";
		public static final String VDC = "/vdc/";
		public static final String VAPP_TEMPLATE = "/vAppTemplate/";
		public static final String VAPP = "/vApp/";
		public static final String TASK = "/task/";
		public static final String TASKS_LIST = "/tasksList/";
		public static final String MEDIA = "/media/";
		public static final String NETWORK = "/network/";
		public static final String ADD_CATALOG_ITEMS_PATH = "/catalogItems";
		public static final String CATALOG = "/catalog/";
		public static final String CATALOG_ITEM = "/catalogItem/";
		public static final String ERROR = "/error/";
		public static final String CONTROL_ACCESS = "/controlAccess/";
		public static final String QUESTION = "/question";
		public static final String OWNER = "/owner";
		public static final String SHADOW_VMS = "/shadowVms";
		public static final String METADATA = "/metadata";
		public static final String RUNTIME_INFO_SECTION = "/runtimeInfoSection/";
		public static final String LEASE_SETTINGS_SECTION = "/leaseSettingsSection/";
		public static final String STARTUP_SECTION = "/startupSection/";
		public static final String NETWORK_SECTION = "/networkSection/";
		public static final String NETWORK_CONNECTION_SECTION = "/networkConnectionSection/";
		public static final String PRODUCT_SECTIONS = "/productSections/";
		public static final String NETWORK_CONFIG_SECTION = "/networkConfigSection/";
		public static final String VIRTUAL_HARDWARE_SECTION = "/virtualHardwareSection/";
		public static final String OPERATING_SYSTEM_SECTION = "/operatingSystemSection/";
		public static final String GUEST_CUSTOMIZATION_SECTION = "/guestCustomizationSection/";
		public static final String CUSTOMIZATION_SECTION = "/customizationSection/";
		public static final String VIRTUAL_HARDWARE_SECTION_CPU = "/virtualHardwareSection/cpu";
		public static final String VIRTUAL_HARDWARE_SECTION_MEMORY = "/virtualHardwareSection/memory";
		public static final String VIRTUAL_HARDWARE_SECTION_DISKS = "/virtualHardwareSection/disks";
		public static final String VIRTUAL_HARDWARE_SECTION_SERIAL_PORTS = "/virtualHardwareSection/serialPorts";
		public static final String VIRTUAL_HARDWARE_SECTION_NETWORKCARDS = "/virtualHardwareSection/networkCards";
		public static final String VIRTUAL_HARDWARE_SECTION_MEDIA = "/virtualHardwareSection/media";
	}

	public static class JaxbContext {
		public static final String ovfSchema = "com.vmware.vcloud.api.rest.schema.ovf";
		public static final String versioningSchema = "com.vmware.vcloud.api.rest.schema.versioning";
		public static final String vCloudSchema = "com.vmware.vcloud.api.rest.schema";
		public static final String vCloudExtensionSchema = "com.vmware.vcloud.api.rest.schema.extension";
	}

	public static class OvfResourceConstants {
		public static final String RASD_RESOURCE_TYPE_CPU = "3";
		public static final String RASD_RESOURCE_TYPE_MEMORY = "4";
		public static final String RASD_RESOURCE_TYPE_IDE_CONTROLLER = "5";
		public static final String RASD_RESOURCE_TYPE_SCSI_CONTROLLER = "6";
		public static final String RASD_RESOURCE_TYPE_DISK = "17";
		public static final String RASD_RESOURCE_TYPE_NETWORK_ADAPTER = "10";
		public static final String RASD_RESOURCE_TYPE_FLOPPY_DRIVE = "14";
		public static final String RASD_RESOURCE_TYPE_CDROM_DRIVE = "15";
		public static final String RASD_RESOURCE_TYPE_SERIAL_PORT = "21";
		public static final String RASD_BUS_SUB_TYPE_BUS_LOGIC = "buslogic";
		public static final String RASD_BUS_SUB_TYPE_LSI_LOGIC = "lsilogic";
		public static final String RASD_BUS_SUB_TYPE_LSI_LOGIC_SAS = "lsilogicsas";
		public static final String RASD_BUS_SUB_TYPE_VIRTUAL_SCSI = "VirtualSCSI";
		public static final String RASD_NETWORK_ADAPTER_SUB_TYPE = "PCNet32";
	}

	public static class QueryConstants {
		public static final String OPEN_PARENTHESES = "(";
		public static final String CLOSE_PARENTHESES = ")";
		public static final String QUESTION_MARK = "?";
		public static final String AMPERSAND = "&";
		public static final String QUERY = "/query";
		public static final String COMMA_SEPERATION = ",";

		public static class SpecializedQuery {
			public static final String CATALOGS = "/catalogs/query";
			public static final String MEDIA_LIST = "/mediaList/query";
			public static final String VAPPTEMPLATES = "/vAppTemplates/query";
			public static final String VAPPS = "/vApps/query";
			public static final String VMS = "/vms/query";
		}

		public static class Params {
			public static final String TYPE = "type=";
			public static final String SORT_ASC = "sortAsc=";
			public static final String SORT_DESC = "sortDesc=";
			public static final String FIELDS = "fields=";
			public static final String PAGE = "page=";
			public static final String OFFSET = "offset=";
			public static final String PAGE_SIZE = "pageSize=";
			public static final String FORMAT = "format=";
			public static final String FILTER = "filter=";
		}
	}
}