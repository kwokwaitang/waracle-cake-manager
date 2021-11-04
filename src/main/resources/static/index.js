$(window).on('load', function() {
    // Is there an exception??? If so, there will be a modal present so show it
    if ($("#staticBackdrop").length) {
        $("#staticBackdrop").modal("show");
    }
});