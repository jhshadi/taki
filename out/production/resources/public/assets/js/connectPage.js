$(document).on("ready", function() {

    // Consts
    var SERVER_ADDRESS = "serverAddress";
    var SERVER_PORT = "serverPort";
    var COOKIES_EXPIRE = 365;

    // Initialize
    getServerDetails();

    // Functions
    function getServerDetails() {
        if (document.cookie !== "") {
            $('#serverAddress').val($.cookie(SERVER_ADDRESS));
            $('#serverPort').val($.cookie(SERVER_PORT));
        }
    }

    // Events
    $('form.form-signin').on('submit', function(event) {
        var form = $(this);
        var params = form.serialize();
        
        form.children('input').attr('disabled', 'disabled');
        form.children('button').button('loading');

        $.get("/Connect", params, function(data) {        
            if (data.error == null) {
                $.cookie(SERVER_ADDRESS, $('#serverAddress').val(), {expires: COOKIES_EXPIRE, path: '/connect.html'});
                $.cookie(SERVER_PORT, $('#serverPort').val(), {expires: COOKIES_EXPIRE, path: '/connect.html'});
                window.location.replace('/index.html');
            } else {
                form.children('input').removeAttr('disabled');
                form.children('button').button('reset');
                createErrorMsg('#errorMsg', data.error);
            }
        });
        
        return false;
    });
    
    function createErrorMsg(divId, msg) {
        var alertMsg = "<div id='signin-alert' class='alert alert-danger fade in'>"
                    + "<button type='button' class='close' data-dismiss='alert' aria-hidden='true'>×</button>"
                    + "<p>" + msg +"</p>"
                    + "</div>";
        $(divId).empty().append(alertMsg);
    }

});
