var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   constants.js
 *   core.js
 *   debug.js
 *   event-manager.js
 *   normalize-constants.js
 *   parse-ticket.js
 */

/**
 * A thin wrapper object around the VMRC plugin.
 *
 * This thin wrapper serves the following purposes:
 *  - Exposing functionality in a browser-agnostic way.
 *  - Adding hide/show functionality (to allow for displaying modal dialogs)
 *  - Enhancing event registration (multiple handlers, one-time handlers)
 */
vmware.console = function(parent) {
    /**
     * The field which will store a plugin instance wrapped as an event manager
     *
     * @private
     */
    var view;

    var isMsie = $.browser.msie;

    var hiddenCount = 1;

    var currentState;

    /**
     * Utility function to normalize arrays returned by the plugin.
     *
     * @private
     * @param {array} array An array returned by the plugin.
     * @return {array} A "real" JavaScript array.
     */
    function normalizePluginArray(array) {
        // 'unknown' is not a valid JavaScript type, so if the condition is met we assume IE.
        return typeof(array) == 'unknown' ? array.toArray() : array;
    }

    /**
     * Utility function to create a plugin element.
     *
     * Initially, created objects are hidden.
     *
     * @private
     * @param {string} id The ID to assign to the element.
     * @return {object} An object DOM element.
     */
    function createControl(id) {
        var c = $('<object></object>').attr('id', id);
        if (isMsie) {
            c.attr('classid', vmware.object.CLASSID);
            c.addClass("msie");
        } else {
            c.attr('type', vmware.object.TYPE);
            c.addClass("nonmsie");
        }

        c.addClass("console");
        c.addClass("hidden");

        return c;
    }

    /**
     * Utility function to allow for browser-agnostic event registration.
     *
     * @private
     * @param {string} event The event string to register a handler for.
     * @param {function} handler The handler to register.
     */    
    function registerEvent(event, handler) {
        vmware.log("TRACE", "plugin", "registering handler for {0}".format(event));
        if (isMsie) {
            view.attachEvent(event, handler);
        } else {
            view[event] = handler;
        }
    }

    /**
     * A utility method to map VMRC events to events on the internal eventManager instance
     *
     * @private
     */    
    function registerInternalViewEvents() {
        registerEvent("onConnectionStateChange", function(connectionState, host, vmId, userRequested, reason) {
            view.trigger(vmware.events.VIEW_CONNECTION_STATE_CHANGE, {connectionState: connectionState, host: host, vmId: vmId, userRequested: userRequested, reason: reason});
        });
        registerEvent("onScreenSizeChange", function(width, height) {
            view.trigger(vmware.events.SIZE_CHANGE, {width: width, height: height});
        });
        registerEvent("onWindowStateChange", function(windowState) {
            view.trigger(vmware.events.WINDOW_STATE_CHANGE, {windowState: windowState});
        });
        registerEvent("onGrabStateChange", function(grabState) {
            view.trigger(vmware.events.GRAB_STATE_CHANGE, {grabState: grabState});
        });
    }

    /**
     * A state-tracking handler for the onConnectionStateChange event.
     *
     * This handler removes the need to query to plugin for the current state.
     *
     * @private
     * @param {object} event The event
     */
    function handleConnectionStateChange (event) {
        currentState = event.eventData.connectionState;
    }

    /**
     * A backing method for hiding and showing the console.
     *
     * @private
     * @param {boolean} visible Whether the console should be shown.
     */    
    function setVisible(visible) {
        if (visible) {
            vmware.log("TRACE", "console", "removing hidden class from console");
            $(view).removeClass("hidden");
            hiddenCount = 0;
        } else {
            vmware.log("TRACE", "console", "adding hidden class to console");
            $(view).addClass("hidden");
        }
    };

    var control = createControl($(parent).attr("id") + "-view");
    control.appendTo(parent);
    view = vmware.eventManager(vmware.normalizeConstants(control[0]));
    registerInternalViewEvents();
    view.register(vmware.events.VIEW_CONNECTION_STATE_CHANGE, handleConnectionStateChange);

    var that = {};

    /**
     * Register a handler for an event.
     *
     * @param {string} event The event key for which this handler will be triggered.
     * @param {function} handler The handler function.
     */
    that.addHandler = function(event, handler) {
        vmware.log("TRACE", "console", "add handler called for {0}".format(event));
        view.register(event, handler);
    };

    /**
     * Register a one-time handler for an event.
     *
     * @param {string} event The event key for which this handler will be triggered.
     * @param {function} handler The handler function.
     */
    that.handleOnce = function(event, handler) {
        vmware.log("TRACE", "console", "handle once called for {0}".format(event));
        view.once(event, handler);
    };

    /**
     * Unregister a handler for an event.
     *
     * @param {string} event The event key to unregister a handler for
     * @param {function} handler The handler function to unregister.
     */
    that.removeHandler = function(event, handler) {
        vmware.log("TRACE", "console", "remove handler called for {0}".format(event));
        view.unregister(event, handler);
    };

    /**
     * A wrapper for the VMRC isReadyToStart method.
     */
    that.isReadyToStart = function() {
        vmware.log("DEBUG", "plugin", "checking isReadyToStart()");
        return view.isReadyToStart();
    }

    /**
     * A wrapper method for the VMRC startup method.
     */
    that.startup = function(opts) {
        vmware.log("TRACE", "console", "startup called with opts {0}".format(opts));
        opts = opts ? opts : {};
        vmware.log("DEBUG", "plugin", "calling startup({0}, {1}, {2}, {3})".format(view.Mode.MKS, view.MessageMode.EVENT_MESSAGES, opts.persistent || false, opts.advancedConfig || ''));
        view.startup(view.Mode.MKS, view.MessageMode.EVENT_MESSAGES, opts.persistent || false, opts.advancedConfig || '');
        that.showConsole();
    };

    /**
     * A wrapper method for the VMRC shutdown method.
     */
    that.shutdown = function() {
        vmware.log("TRACE", "console", "shutdown called");
        vmware.log("DEBUG", "plugin", "calling shutdown()");
        view.shutdown();
    };

    /**
     * A wrapper method for the VMRC disconnect method.
     */
    that.disconnect = function() {
        vmware.log("TRACE", "console", "disconnect called");
        vmware.log("DEBUG", "plugin", "calling disconnect()");
        view.disconnect();
    };

    /**
     * A wrapper method for the VMRC connect method.
     *
     * @param {string} ticket The screen ticket retreived from the vCD API.
     * @return {boolean} false if the supplied ticket was invalid. The value returned by the plugin otherwise.
     */
    that.connect = function(ticketString) {
        vmware.log("TRACE", "console", "connect called with ticket string {0}".format(ticketString));

        var ticketPieces = vmware.parseTicket(ticketString);
        if (!ticketPieces) {
            return false; //Invalid ticketString
        }

        vmware.log("DEBUG", "plugin", "calling connect({0}, {1}, '', '', {2}, '', '')".format(ticketPieces.host, ticketPieces.ticket, ticketPieces.moid));
        return view.connect(ticketPieces.host, ticketPieces.ticket, '', '', ticketPieces.moid, '', '');
    };

    /**
     * A wrapper method for the VMRC setFullscreen method.
     *
     * @param {boolean} fullscreen
     */
    that.setFullscreen = function(fullscreen) {
        vmware.log("TRACE", "console", "setFullscreen called with a value of {0}".format(fullscreen));

        if (currentState !== view.ConnectionState.CS_CONNECTED) {
            vmware.log("DEBUG", "console", "skipping setFullscreen; console is in state {0}".format(state));
            return;
        }

        vmware.log("DEBUG", "plugin", "calling setFullscreen({0})".format(fullscreen));
        view.setFullscreen(fullscreen);
    };

    /**
     * A wrapper method for the VMRC sendCAD method.
     */
    that.sendCtrlAltDelete = function() {
        vmware.log("TRACE", "console", "send ctrl-alt-del called");

        if(currentState !== view.ConnectionState.CS_CONNECTED) {
            vmware.log("DEBUG", "console", "skipping sendCtrlAltDelete; console is in state {0}".format(state));
            return;
        }

        vmware.log("DEBUG", "plugin", "calling sendCAD()");
        view.sendCAD();
    };

    /**
     * A method to show a hidden console.
     *
     * Calls to hide and show are counted, so the console will be shown
     * if the number of calls to show reaches the number of calls to hide.
     */
    that.showConsole = function() {
        vmware.log("TRACE", "console", "show console called with an initial count of {0}".format(hiddenCount));
        if (hiddenCount <= 0) {
            vmware.log("WARN", "console", "show console called, but hidden count already 0");
            return;
        }

        hiddenCount = hiddenCount - 1;
        if (hiddenCount === 0) {
            vmware.log("DEBUG", "console", "showing console");
            setVisible(true);
        }
    };

    /**
     * A method to hide the console.
     *
     * Calls to hide and show are counted, so the console will be shown
     * if the number of calls to show reaches the number of calls to hide.
     */
    that.hideConsole = function() {
        vmware.log("TRACE", "console", "hide console called with an initial count of {0}".format(hiddenCount));
        if (hiddenCount === 0) {
            vmware.log("DEBUG", "console", "hiding console");
            setVisible(false);
        }

        hiddenCount = hiddenCount + 1;
    };

    return that;
};
