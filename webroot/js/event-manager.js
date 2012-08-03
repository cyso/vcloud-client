var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   core.js
 *   debug.js
 */

/**
 * Wrapping utility that turns a supplied object into an event manager.
 *
 * Note:
 *   Any of the following methods on the supplied object will be overridden:
 *     once
 *     register
 *     trigger
 *     unregister
 *
 * @param {object} that The object to annotate.
 * @return The same annotated object.
 */
vmware.eventManager = function (that) {
    var registry = {};

    /**
     * Register a handler for an event.
     *
     * @param {string} key The event key for which this handler will be triggered.
     * @param {function} method The handler function.
     * @param {object} data The "handler data" that will be provided to the method as part of the event when it is called.
     */
    that.register = function (key, method, data) {
        vmware.log("TRACE", "event-manager", "registering handler for {0}".format(key));
        var handler = {
            method: method,
            handlerData: data
        };

        if (registry.hasOwnProperty(key)) {
            registry[key].push(handler);
        } else {
            registry[key] = [handler];
        }
    };

    /**
     * Unregister a handler for an event.
     *
     * @param {string} key The event key to unregister the handler for.
     * @param {function} method The handler function to unregister.
     */
    that.unregister = function (key, method) {
        vmware.log("TRACE", "event-manager", "unregistering handler for {0}".format(key));
        for (var i = registry[key].length-1; i >= 0; i--) {
            if (registry[key][i].method === method) {
                registry[key].splice(i,1);
            }
        }
    };

    /**
     * Trigger event handlers registered for the supplied key.
     *
     * @param {string} key The event key to trigger handlers for.
     * @param {object} data The "event data" that will be provided to handlers.
     */
    that.trigger = function (key, data) {
        vmware.log("TRACE", "event-manager", "triggering handlers for {0}".format(key));
        if (registry.hasOwnProperty(key)) {
            var handlers = registry[key];
            for (i = 0; i < handlers.length; i += 1) {
                var handler = handlers[i];
                handler.method.apply(this, [{
                    key: key,
                    handlerData: handler.handlerData,
                    eventData: data
                }]);
            }
        }
    };

    /**
     * Register a handler for an event which will be triggered only onc.
     *
     * @param {string} key The event key for which this handler will be triggered.
     * @param {function} method The handler function.
     * @param {object} data The "handler data" that will be provided to the method as part of the event when it is called.
     */
    that.once = function (key, method, data) {
        var onceMethod = function(event) {
            method(event);
            vmware.log("TRACE", "event-manager", "removing one-time handler for {0}".format(key));
            that.unregister(key, onceMethod);
        };

        vmware.log("TRACE", "event-manager", "registering one-time handler for {0}".format(key));
        that.register(key, onceMethod, data);
    };

    return that;
};
