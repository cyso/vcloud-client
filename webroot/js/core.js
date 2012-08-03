/**
 * Augments the Function prototype to easily add new methods to an object.
 * Calls to this will not replace existing methods on an object.
 *
 * @param {string} name The name of the method to add.
 * @param {function} func The function to assign to the method.
 * @return {Object} The class being added to, to allow chaining.
 */
Function.prototype.method = function (name, func) {
    if (!this.prototype[name]) {
        this.prototype[name] = func;
        return this;
    }
};

/**
 * A simple token-replacement style format option for strings.
 *
 * @this {string} A string containing placeholders
 * @param {string...}  The values with which placeholders should be substituted.
 * @return {string} The results of substituting all placeholders in this with the supplied values.
 */
String.method('format', function() {
    var formatted = this;

    /**
     * Determine whether a supplied object is an array.
     *
     * Note: the arguments field that is available within functions returns false since it is not a true array.
     *
     * @private
     * @param {object} object The object to test.
     * @return {boolean} True if the supplied object is an array.
     */
    var isArray = function (object) {
        return (typeof object === 'object' && typeof object.length === 'number' && !(object.propertyIsEnumerable('length')));
    };

    /**
     * Performs the actual substitution of placeholders.
     *
     * @private
     * @param {array} keys The set of values with which placeholders should be substituted.
     */
    var doFormat = function (keys) {
        for (var i = 0; i < keys.length; i++) {
            var regexp = new RegExp('\\{' + i + '\\}', 'gi');
            formatted = formatted.replace(regexp, keys[i]);
        }

        formatted = formatted.toString();
    };

    if (arguments.length === 1 && isArray(arguments[0])) {
        doFormat(arguments[0]);
    } else {
        doFormat(arguments);
    }


    return formatted;
});

/**
 * A simple module to handle internationalization of strings.
 */
var i18n = (function () {
    /**
     * The internal map between the native (typically English) strings and local strings.
     *
     * @private
     */
    var data = [];

    return {
        /**
         * Translate the given string based on the currently stored data.
         *
         * If the exact string has been added to the internal map, the corresponding local string is used with the supplied tokens.
         * If the string corresponds, via placeholder substitution, to some string that has been added, placeholder values are extracted from the provided string and used with the local string.
	 * Otherwise, the key is used with the supplied tokens.
         *
         * @param {string} key The native string.
         * @param {array} tokens A set of values to be used to substitute placeholders.
         * @return {string} A localized string.
         */
        getValue: function(key, tokens) {
            var value = null;

            // Handle the case of the exact string having been added
            if (data[key] !== undefined) {
                value = data[key].format(tokens);
            }

            // Handle the case of a more general string having been added
            // e.g. if add("Error: {0}", "Erreur: {0}") has been called,
            //   if key is "Error: connection terminated", this will result
            //   in a value of "Erreur: {0}".format(["connection terminated"]),
            //   which will evaluate to "Erreur: connection terminated".
            if (value === null) {
                for (pattern in data) {
                    var pieces = key.match('^' + pattern.replace(/\{[0-9]+\}/g, '(.*)') + '$');
                    if (pieces) {
                        value = data[pattern].format(pieces.splice(1));
                    }
                }
            }

           // Default to displaying the key
            if (value === null) {
                value = key.format(tokens);
            }

            return value;
        },

        /**
         * Store a single mapping
         *
         * @param {string} key The native string to map.
         * @param {string} value The local string to use.
         */
        add: function(key, value) {
            data[key] = value;
        },

        /**
         * Store several mappings
         * @param {object} items An associative array of native string to local string.
         */
        addAll: function(items) {
            for (var key in items) {
                data[key] = items[key];
            }
        },

        /**
         * Clears all mappings.
         */
        clear: function() {
            data.length = 0;
        }
    };
}());

/**
 * A convenience method for the i18n module.
 *
 * @this {string} A string to localize (i.e. the string to use as the key).
 * @param {string...} arguments The values with which placeholders should be substituted.
 * @return {string} The results of the localization of the string with the supplied parameters.
 */
String.method('localize', function() {
    return i18n.getValue(this, arguments).toString();
});

/**
 * A JavaScript implementation of the Currying pattern.
 *
 * @this {function} A function to curry.
 * @param {object...} arguments The values with which to curry.
 * @return {function} The result of currying the method with the supplied arguments.
 */
Function.method('curry', function () {
    var slice = Array.prototype.slice,
        args = slice.apply(arguments),
        that=this;

    return function () {
        return that.apply(null, args.concat(slice.apply(arguments)));
    };
});

/**
 * A utility method to allow delayed execution of a function.
 *
 * The given function will not be called until the supplied condition is true, which will be checked at the given periodicity.
 *
 * @this {function} The function to execute.
 * @param {function} condition The condition to evaluate.
 * @param {number} periodicity The timeout, in miliseconds, between when the condition should be evaluated.
 */
Function.method('runWhen', function (condition, periodicity) {
    var that = this;

    if (condition()) {
        that();
    } else {
        setTimeout(function () { that.runWhen(condition, periodicity); }, periodicity);
    }
});
