var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   alert.js
 *   constants.js
 *   core.js
 */

/**
 * A module for installation-related utilities.
 */
vmware.installer = (function () {
    var that = {};

    /**
     * A simple version checking utility.
     *
     * @param {string} desired The target version.
     * @param {string} actual The version being checked.
     * @return {boolean} True if the actual version is at least the desired version.
     */
    var checkVersion = function (desired, actual) {
        var verifySyntax = function (version) {
            return /^\d*(\.\d*)*$/.test(version);
        }

        var toArray = function (version) {
            var array = [];
            var strings = version.split('.');

            for (var index in strings) {
                array.push(parseInt(strings[index]));
            }

            return array;
        };

        var compareArrays = function (left, right) {
            // If the left array has fewer segments, reverse them
            if (left.length < right.length) {
                return -1 * compareArrays(right, left);
            }

            for (var index = 0; index < left.length; index++) {
                if (index > right.length - 1) {
                    return -1; // left is a more specific version of right
                }
                else if (left[index] > right[index]) {
                    return -1; // left is a greater version than right
                }
                else if (left[index] < right[index]) {
                    return 1; // left is a lesser version than right
                }
            }

            return 0; // versions are equal
        };

        if (!verifySyntax(desired) || !verifySyntax(actual)) {
            return;
        }

        return compareArrays(toArray(desired), toArray(actual)) >= 0;
    };

    /**
     * A utility that can be used to choose the correct installer to download.
     *
     * @return {string} The file name of the installer.
     */
    var chooseInstaller = function () {
        var installer;

        // References:
        // 0- http://api.jquery.com/jQuery.browser/
        // 1- https://developer.mozilla.org/en/DOM/window.navigator.platform
        // 2- https://developer.mozilla.org/en/DOM/window.navigator.oscpu

        // assumes correctness of [0]
        if ($.browser.msie) {
            installer = vmware.installers.WINDOWS;
        }
        else if ($.browser.mozilla) {
            // assumes browser follows [1]
            if (/^Win/.test(navigator.platform)) {
                installer = vmware.installers.WINDOWS;
            }
            else if (/^Lin/.test(navigator.platform)) {
                // assumes browser follows [2]
                if (/64$/.test(navigator.oscpu)) {
                    installer = vmware.installers.LINUX.x86_64;
                }
                else {
                    installer = vmware.installers.LINUX.x86;
                }
            }
        }

        return installer;
    };

    /**
     * Utility function to create a hidden plugin instance.
     *
     * @private
     * @return {object} An object DOM element.
     */
    var createControl = function() {
        var c = $('<object></object>');
        if ($.browser.msie) {
            c.attr('classid', vmware.object.CLASSID);
        } else {
            c.attr('type', vmware.object.TYPE);
        }

        c.addClass("probe");
        c.addClass("hidden");

        return c;
    };

    /**
     * A utility that can be used to determine whether the VMRC plugin is installed.
     *
     * This utility is implemented using an instance of a VMRC plugin object.
     *
     * @param {object} parent The container element within which the probe object should be created. Default: body
     */
    var isVmrcInstalled = function (parent) {
        parent = parent || $('body');

        var control = createControl();
        control.appendTo(parent);
        var plugin = control[0];

        var result = true;

        if (typeof plugin.getVersion === 'undefined' ||
            typeof plugin.getSupportedApi === 'undefined') {
            // No verion of the plugin installed
            result = false;
        }
        else if (plugin.getSupportedApi().indexOf(vmware.object.API) === -1) {
            // Installed plugin does not support correct API
            result = false;
        }
        else if (!checkVersion(vmware.object.VERSION, plugin.getVersion())) {
            // Installed plugin is not a sufficiently new version
            result = false;
        }

        control.remove();

        return result;
    };

    /**
     * A helper that will prompt for installation of VMRC if necessary.
     *
     * @param {string} baseAddress The URL to the folder of a cell or CDN containing the VMRC installers. Default: this host
     */
    var installVmrcIfNeeded = function (baseAddress) {
        baseAddress = baseAddress || '';

        if (!isVmrcInstalled($('body'))) {
            var installer = baseAddress + chooseInstaller();

            var message = 'Please <a href="{0}">download the installer</a>.'.localize(installer);

            vmware.alert("Error: VMRC Plugin not installed".localize(), message);
        }
    };

    that.isVmrcInstalled = isVmrcInstalled;
    that.chooseInstaller = chooseInstaller;
    that.installVmrcIfNeeded = installVmrcIfNeeded;

    return that;
})();
