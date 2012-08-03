var vmware = (typeof vmware == "undefined" || !vmware) ? {} : vmware;

/**
 * Parse a vCD-supplied ticket into host, moid, and token.
 *
 * @param {string} ticket A ticket string supplied by vCD
 * @return {object} An object with host, moid, and token properties.
 * @return {boolean} false if the ticket is invalid.
 */
vmware.parseTicket = function (ticket) {
    var regex = /mks:\/\/([^\/]*)\/([^\?]*)\?ticket=(.*)/;

    var result = regex.exec(ticket);

    if (!result) {
        return false;
    }

    return {host: result[1], moid: result[2], ticket: unescape(result[3])};
};
