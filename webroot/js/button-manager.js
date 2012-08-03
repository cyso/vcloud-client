var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   button.js
 *   core.js
 *   debug.js
 *   event-manager.js
 */

/**
 * A manager for a collection of buttons.
 *
 * @param {object} spec An object specifying the container for the manager to be created.
 * @param {object} The newly created manager.
 */
vmware.buttonManager = function (spec) {
    var that = {};

    var buttons = [];

    /**
     * An internal delegate eventManager instance.
     *
     * @private
     */
    var internalHandler = vmware.eventManager({});

    /**
     * A handler action which unwraps an event and triggers the delegate event manager.
     *
     * @private
     * @param {object} event A jQuery event.
     */
    var handler = function (event) {
        var key = event.data.key;
        vmware.log("TRACE", "button-manager", "triggering handlers for button {0}".format(key));
        internalHandler.trigger(key, {key: key, state: buttons[key].getState()});
    };

    /**
     * A factory method for button objects.
     *
     * @param {string} key A unique identifier for the button being created.
     * @param {object} stateDefinitions The image and text information for the states for the button to be created.
     * @return {this} For chaining.
     */
    that.createButton = function (key, stateDefinitions) {
        var states = {};
        for (state in stateDefinitions) {
            states[state] = {src: stateDefinitions[state].image, alt: stateDefinitions[state].text, title: stateDefinitions[state].text};
        }

        vmware.log("TRACE", "button-manager", "creating button {0}".format(key));
        buttons[key] = vmware.button({
            states: states,
            buttonHandler: handler,
            eventData: {key: key},
            parent: spec.container
        });

        return that;
    };

    /**
     * A registration method for click events.
     *
     * @param {string} key The unique identifier for the button the handler should be triggered for.
     * @param {function} handler The function to call.
     * @param {object} data The handler data to provide to the handler.
     * @return {this} For chaining.
     */
    that.registerHandler = function (key, handler, data) {
        vmware.log("TRACE", "button-manager", "registering handler for button {0}".format(key));
        internalHandler.register(key, handler, data);

        return that;
    };

    /**
     * A delegating method for setting state.
     *
     * @param {string} key The unique identifier for the button.
     * @param {string} state The state to which the button should be set.
     * @return {this} For chaining.
     */
    that.setState = function (key, state) {
        vmware.log("TRACE", "button-manager", "changing state for button {0} to {1}".format(key, state));
        buttons[key].setState(state);

        return that;
    };

    return that;
};
