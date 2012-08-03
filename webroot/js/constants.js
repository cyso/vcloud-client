var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;

/**
 * Enumeration of events as strings to be used in conjunction with an event manager.
 */
vmware.events = {
    VIEW_CONNECTION_STATE_CHANGE: 'viewconnectionstatechange',
    SIZE_CHANGE: 'sizechange',
    WINDOW_STATE_CHANGE: 'windowstatechange',
    GRAB_STATE_CHANGE: 'grabstatechange',
    VIEW_MESSAGE: 'viewmessage'
};

/**
 * Enumerator of installer files.
 */
vmware.installers = {
    WINDOWS: 'vmware-vmrc-win32-x86.exe',
    LINUX: {x86: 'VMware-VMRC.i386.bundle', x86_64: 'VMware-VMRC.x86_64.bundle'}
};

/**
 * Constants used by the plugin 'object' DOM element.
 */
vmware.object = {
    CLASSID: 'CLSID:A24C4C22-E2B7-4701-9DF1-E51BDC809850',
    TYPE: 'application/x-vmware-remote-console-2011-1',
    VERSION: '4.0.0.444321',
    API: 'vsphere-2011'
};
