var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;
/* Requires:
 *   core.js
 *   debug.js
 */

/**
 * Display a confirmation-like jQuery dialog.
 *
 * @param {string} title The title for the confirmation.
 * @param {string} message The body text for the confirmation.
 * @param {function} closeHandler A function which will be called when the confirmation is dismissed.
 * @param {function} okHandler A function which will be called when the "Ok" option is selected, but before the dialog has closed.
 */
vmware.confirm = function(title, message, closeHandler, okHandler) {
    var confirmOverlay = $('<div></div>').addClass('ui-widget-overlay').appendTo($('body'));

    var confirm = $('<div></div>').attr("title", title);
    confirm.append($('<p></p>').append(message));
    $('body').append(confirm);

    var confirmOptions = {
        modal: false,
        width: 400,
        buttons: {
        },
        close: function (event, ui) {
            vmware.log("TRACE", "confirm", "closing confirmation");
            confirmOverlay.remove();
            confirm.remove();
            closeHandler();
        }
    };

    confirmOptions.buttons['Ok'.localize()] = function() {
        vmware.log("TRACE", "confirm", "Ok selected");
        confirm.dialog("close");
        okHandler();
    };

    confirmOptions.buttons['Cancel'.localize()] = function() {
        vmware.log("TRACE", "confirm", "Cancel selected");
        confirm.dialog("close");
    };

    vmware.log("TRACE", "confirm", "displaying confirmation");
    confirm.dialog(confirmOptions);
};
