var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;

/**
 * Normalize the constants defined on a provided plugin instance.
 *
 * Post-normalization, constants can be accessed like:
 *     vmware.Mode.MKS
 *     vmware.ConnectionState.CS_CONNECTED
 *
 * @param {object} vmrc The VMRC plugin object.
 * @return {object} The same VMRC plugin object with normalized constants.
 */
vmware.normalizeConstants = function (vmrc) {
    var isMsie = $.browser.msie;

    /**
     * Remove the "VMRC_" prefix; the "VMRC_" scoping is implied.
     *
     * @param {string} The string from which the prefix should be removed.
     * @return {string} The string with the prefix removed (if present).
     */
    var shorten = function (input) {
        return input.replace(/VMRC_/g, "");
    };

    /**
     * Convert a MSIE-style property into a standard JS object
     *
     * @param {object} property The property to convert.
     * @return {object} A normalized object which can be used as an assoicative array.
     */
    var getNormalizedProperty = function (property) {
        if (isMsie) {
            var keys = {};
            var vbkeys = new VBArray(property.Keys());
            for (k = 0; k <= vbkeys.ubound(1); k++) {
                key = vbkeys.getItem(k);
                keys[key] = property(key);
            }
            return keys;
        } else {
            return property;
        }
    };

    /**
     * Create a "short key"-to-value map for all keys for a given property
     *
     * @param {object} normalizedProperty An associative array-like object for which the keys should be shortened.
     * @return {object} A new associative array-like object with shortened keys.
     */
    var shortenProperty = function (normalizedProperty) {
        var values = {};
        for (var key in normalizedProperty) {
            values[shorten(key)] = normalizedProperty[key];
        }

        return values;
    };

    /**
     * Create a "short property"-to-("short key"-to-value map) map
     *
     * @param {object} pluginInstance The VMRC plugin object on which to operate.
     * @return {object} An associative array-like object of shortened property names and key names.
     */
    var getConstants = function (pluginInstance) {
        var constants = {};
        for (var propertyName in pluginInstance) {
            if (propertyName.match(/^VMRC_/)) {
                var property = getNormalizedProperty(pluginInstance[propertyName]);
                constants[shorten(propertyName)] = shortenProperty(property);
            }
         }
         return constants;
    };

    /**
     * Take a map, like the one returned from getConstants, and add the values to the given object.
     *
     * This is similar to jQuery.extend, but compatible with the plugin.
     *
     * @param {object} object The object to extend.
     * @param {object} constants The constants to add.
     */
    var applyConstantsToObject = function (object, constants) {
        for (var propertyName in constants) {
             object[propertyName] = constants[propertyName];
        }
        return object;
    };

    var constants = getConstants(vmrc);

    return applyConstantsToObject(vmrc, constants);
};
