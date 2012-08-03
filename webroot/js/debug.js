var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   core.js
 */

/**
 * A module to handle debug logging.
 *
 * By default, logging is disabled.
 * If Firebug or IE Developer Tools is enabled, log messages print to that console.
 */
vmware.log = (function () {
    var print = function (){};

    // If Firebug or Developer Tools is enabled, log to that
    if (window.console && window.console.log) {
        print = window.console.log;
    }

    /**
     * A simple formatting method for log statements.
     *
     * @param {string} level The log level.
     * @param {string} component The component for which the message is being logged.
     * @param {string} string The log message.
     * @return {string} A formatted log message.
     */
    var format = function (level, component, string) {
        return "{0} [{1}] {2}: {3}".format((new Date()).toLocaleTimeString(), level, component, string);
    };

    /**
     * Define the printing method for the logger.
     *
     * @param {function} printer The method to call with the log string.
     * @param {string} printer The string "popout" to use the built-in popout logging window.
     * @param {object} printer Any falsey value to disable printing of log messages.
     */
    var setPrinter = function (printer) {
        var failure = false;

        if (!printer) {
            print = function (){};
        } else if (typeof(printer) == "function") {
            print = printer;
        } else if (printer === "popout") {
            var popout = window.open('','','width=600,height=400,resizable=1,location=0');
            var container = $('<ul/>', popout.document).css('font-family','monospace').appendTo($('body', popout.document));

            print = function (stringValue) {
                container.append($('<li/>', popout.document).append(stringValue));
            };
        }
    };

    /**
     * The logging method.
     *
     * @param {string} level The log level.
     * @param {string} component The component for which the message is being logged.
     * @param {string} string The log message.
     */
    var that = function (level, component, string) {
        print(format(level, component, string));
    };
    // Defines a setPrinter method on the logging method.
    that.setPrinter = setPrinter;

    return that;
}());
