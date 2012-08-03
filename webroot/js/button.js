var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   core.js
 *   debug.js
 */

/**
 * A simple multi-state button implementation.
 *
 * @param {object} spec An object specifying the button to be created.
 * @param {object} The newly created button.
 */
vmware.button = function (spec) {
    var that = {};

    var states = spec.states;
    var state = 'defaultState'
    var buttonObject = $("<img/>");
    buttonObject.attr(states[state]);
    buttonObject.bind('click', spec.eventData, spec.buttonHandler);
    vmware.log("TRACE", "button", "displaying button");
    buttonObject.appendTo(spec.parent);

    /**
     * A setter for the button state, which controls the attributes of the button.
     *
     * @param {string} newState The state being set.
     */
    that.setState = function (newState) {
        vmware.log("TRACE", "button", "changing state from {0} to {1}".format(state, newState));
        state = newState;
        buttonObject.attr(states[state]);
    };

    /**
     * A getter for the button state.
     *
     * @return {string} The current state.
     */
    that.getState = function () {
        return state;
    };

    return that;
};
