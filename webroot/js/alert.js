var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   core.js
 *   debug.js
 */

/**
 * Display an alert-like jQuery dialog.
 *
 * @param {string} title The title for the alert.
 * @param {string} message The body text for the alert.
 * @param {function} closeHandler A function which will be called when the alert is dismissed.
 */
vmware.alert = function(title, message, closeHandler) {
    var alertOverlay = $('<div></div>').addClass('ui-widget-overlay').appendTo($('body'));

    var alert = $('<div></div>').attr("title", title);
    alert.append($('<p></p>').append(message));
    $('body').append(alert)

    var alertOptions = {
        modal: false,
        width: 400,
        buttons: {
        },
        close: function (event, ui) {
            vmware.log("TRACE", "alert", "closing alert");
            alertOverlay.remove();
            alert.remove();
            closeHandler();
        }
    };
    alertOptions.buttons['Ok'.localize()] = function() {
        vmware.log("TRACE", "alert", "Ok selected");
        alert.dialog("close");
    };

    vmware.log("TRACE", "alert", "displaying alert");
    alert.dialog(alertOptions);
};
