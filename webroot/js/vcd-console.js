var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   alert.js
 *   buttonManager.js
 *   confirm.js
 *   console.js
 *   constants.js
 *   core.js
 */

/**
 * A constructor for an example console implementation building on console.
 *
 * @param {string} contentId The ID of the object in which the console should be displayed.
 * @param {string} toolbarId The ID of the object in which the toolbar buttons should be displayed.
 * @param {string} statusId The ID of the object in which the status information should be displayed.
 * @return {object} A "vcdConsole" object.
 */
vmware.vcdConsole = function(contentId, toolbarId, statusId) {
    var content = $("#" + contentId);
    var toolbar = $("#" + toolbarId);
    var status = $("#" + statusId);

    var wrapper = $('<div></div').attr('class', 'vmrcWrapper').height('1').width('1');
    content.append(wrapper);

    var console;
    var powerButtonManager;
    var pluginButtonManager;

    /**
     * Utility method to show an alert.
     *
     * This method handles hiding/showing the console as necessary.
     *
     * @private
     * @param {string} title The alert title.
     * @param {string} message The alert body text.
     */
    showAlert = function (title, message) {
        console.hideConsole();
        vmware.alert(title, message, console.showConsole);
    };

    /**
     * Utility method to show a confirmation based on a registered event.
     *
     * This method handles hiding/showing the console as necessary.
     *
     * @private
     * @param {object} event A wrapped event as returned from an eventManager.
     */
    showConfirmation = function (event) {
        console.hideConsole();
        vmware.confirm(event.handlerData.title, event.handlerData.message, console.showConsole, event.handlerData.handler);
    };

    /**
     * Initiaizer method which creates a toolbar containing power and console buttons.
     *
     * Note: As the handler body for power operations is specific to the portal, no implementation is provided.
     *
     * @private
     */
    initializeToolbar = function () {
        var span = $('<span></span>').attr('id', 'power-buttons');
        span.appendTo(toolbar);
        powerButtonManager = vmware.buttonManager({container: span})
            .createButton("power-on", {defaultState: {image: "images/vm-poweron.png", text: "Power On".localize()}})
            .registerHandler("power-on", showConfirmation, {
                title: "Confirm Power On".localize(), 
                message: "Power On Virtual Machine?".localize(), 
                handler: function(){}
            })
            .createButton("power-pause", {defaultState: {image: "images/vm-suspend.png", text: "Suspend".localize()}})
            .registerHandler("power-pause", showConfirmation, {
                title: "Confirm Suspend".localize(), 
                message: "Suspend Virtual Machine?".localize(), 
                handler: function(){}
            })
            .createButton("power-off", {defaultState: {image: "images/vm-poweroff.png", text: "Power Off".localize()}})
            .registerHandler("power-off", showConfirmation, {
                title: "Confirm Power Off".localize(), 
                message: "Power Off Virtual Machine?".localize(), 
                handler: function(){}
            })
            .createButton("power-reset", {defaultState: {image: "images/vm-reset.png", text: "Reset".localize()}})
            .registerHandler("power-reset", showConfirmation, {
                title: "Confirm Reset".localize(), 
                message: "Reset Virtual Machine?".localize(), 
                handler: function(){}
            });

        $('<img />').attr('src', 'images/separator.png').attr('alt', '').addClass('separator').appendTo(toolbar);

        span = $('<span></span>').attr('id', 'plugin-buttons');
        span.appendTo(toolbar);
        pluginButtonManager = vmware.buttonManager({container: span})
            .createButton("ctrl-alt-del", {defaultState: {image: "images/ctrl-alt-del-disabled.png", text: "Send ctrl+alt+del (disabled)".localize()}, connected: {image: "images/ctrl-alt-del.png", text: "Send ctrl+alt+del".localize()}})
            .registerHandler("ctrl-alt-del", function(event) {
                if (event.eventData.state == "connected") {
                    console.sendCtrlAltDelete();
                }
            })
            .createButton("fullscreen", {defaultState: {image: "images/full-screen-disabled.png", text: "Open in fullscreen (disabled)".localize()}, connected: {image: "images/full-screen.png", text: "Open in fullscreen".localize()}})
            .registerHandler("fullscreen", function(event) {
                if (event.eventData.state == "connected") {
                    console.setFullscreen(true);
                }
            });
    };
    initializeToolbar();

    /**
     * Internal one-time handler to update status message on connect.
     *
     * @private
     * @param {object} event A wrapped connectionStateChange event.
     */
    initialConnectionHandler = function (event) {
        if (event.eventData.connectionState == 2) {
            status.text("Connected".localize());
            pluginButtonManager.setState("ctrl-alt-del", "connected");
            pluginButtonManager.setState("fullscreen", "connected");
        } else {
            status.text("Error connecting".localize());
        }
    };

    /**
     * Internal handler to adjust size of displayed console
     *
     * @private
     * @param {object} event A wrapped screenSizeChange event.
     */
    resizeHandler = function (event) {
        var size = event.eventData;

        wrapper.width(size.width);
        wrapper.height(size.height);
    };

    var that = {
        /**
         * Create a console, wait for it to be ready, and then startup and connect.
         *
         * Note: Tickets should be of the form mks://hostname/moid?ticket=value.
         *       Tickets can be retrieved via the acquireTicket vCD API call.
         *
         * @param {function} ticketer A function which will return a new ticket each time it is called.
         */
        init: function(ticketer) {
            // While only one ticket is used, for forward compatability this is passed in as a function.
            var ticketString = ticketer();

            console = vmware.console(wrapper);
            console.handleOnce(vmware.events.VIEW_CONNECTION_STATE_CHANGE, initialConnectionHandler);
            console.addHandler(vmware.events.SIZE_CHANGE, resizeHandler);

            var isReadyToStart = function () {
                return console.isReadyToStart();
            };

            var start = function () {
                console.startup({persistent: false, advancedConfig: 'usebrowserproxy=true;tunnelmks=true'});
                console.connect(ticketString);
            }

            start.runWhen(isReadyToStart, 10);
        }
    };

    return that;
};
