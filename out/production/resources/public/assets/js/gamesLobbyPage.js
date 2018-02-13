$(document).on("ready", function() {

    // Enums
    var listTypeEnum = {
        WAITING: 0,
        ACTIVE: 1
    };

    // Initialize
    $.ajaxSetup({ cache: false });
    getGamesLists();

    // Functions
    // Game Lists And Game Details Functions
    function getGamesLists() {
        var btn = $('#refreshBtn');

        btn.button('loading');

        $.get("/GetGames", function(data) {
            fillGamesList('#waitingGamesList', data.waitingGames, listTypeEnum.WAITING);
            bindWaitingGamesWithClickEvent();
            fillGamesList('#activeGamesList', data.activeGames, listTypeEnum.ACTIVE);
            btn.button('reset');
        });
    }

    function fillGamesList(ul, arr, type) {
        var gamesList = $(ul);
        var li;

        gamesList.empty();
        for (var i = 0; i < arr.length; i++) {
            switch (type) {
                case listTypeEnum.WAITING:
                    li = createWaitingGameListItem(arr[i], i);
                    break;
                case listTypeEnum.ACTIVE:
                    li = createActiveGameListItem(arr[i]);
                    break;
            }

            gamesList.append(li);
        }
    }

    function createWaitingGameListItem(gameName, index) {
        return '<li class="list-group-item">'
                + '<a data-toggle="collapse" href="#collapse' + index + '" class="bold collapsed">' + gameName + '</a>'
                + '<div id="collapse' + index + '" class="collapse list-item-content">'
                + '</div>'
                + '</li>';
    }

    function createActiveGameListItem(gameName) {
        return '<li class="list-group-item">' + gameName + '</li>';
    }

    function bindWaitingGamesWithClickEvent() {
        $('#waitingGamesList a').bind('click', function(event) {
            var a = $(event.target);

            if (a.hasClass('collapsed') === true) {
                getGameDetails(a.attr('href'));
            }
        });
    }

    function getGameDetails(collapseId) {
        $('#waitingGamesList div' + collapseId).empty().append(createLoading(true));
        var gameName = $('#waitingGamesList a[href="' + collapseId + '"]').text();

        $.get("/GetGameDetails", {gameName: gameName}, function(data) {
            if (data.error == null) {
                fillGameDetails(collapseId, data);
            }
            else {
                createAlertMsg('#errorMsg-PlayerName', false, data.error);
            }
        });
    }

    function fillGameDetails(collapseId, details) {
        var divElement = $('#waitingGamesList div' + collapseId);

        divElement.empty();
        divElement.append(createGameDetails(details));
        divElement.children('button').bind('click', joinGame);
    }

    function createGameDetails(details) {
        return  '<p><span class="bold">Human Players: </span>' + details.joinedHumanPlayers + '/' + details.humanPlayers + '</p>'
                + '<p><span class="bold">Computer Players:</span> ' + details.computerizedPlayers + '</p>'
                + '<button gameName="' + details.name + '" type="button" class="btn btn-primary btn-xs center-block">Join</button>';
    }

    // Join Game Functions
    function joinGame(event) {
        var btn = $(event.target);
        var params = {gameName: btn.attr('gameName'), playerName: $('#playerName').val()};

        btn.attr('disabled', 'disabled');

        $.get("/JoinGame", params, function(data) {
            if (data.error == null) {
                window.location.replace('/taki.html');
            } else {
                createAlertMsg('#errorMsg-PlayerName', false, data.error);
            }

            btn.removeAttr('disabled');
        });

    }

    // Create Game Functions
    function createGame(event) {
        var form = $(this);
        var params = form.serialize();

        form.find('input').attr('disabled', 'disabled');
        form.find('select').attr('disabled', 'disabled');
        form.find('button').button('loading');

        $.get("/CreateGame", params, function(data) {
            if (data.error == null) {
                createAlertMsg('#errorMsg-createGame', true, "Game '" + form.find('input').val() + "' created successfully!");
                form.find('input').val("");
                form.find('select[name="humanPlayers"]').val("1");
                form.find('select[name="computerPlayers"]').val("0");
                getGamesLists();
            } else {
                createAlertMsg('#errorMsg-createGame', false, data.error);
            }

            form.find('input').removeAttr('disabled');
            form.find('select[name="humanPlayers"]').removeAttr('disabled');
            form.find('select[name="computerPlayers"]').removeAttr('disabled');
            form.find('button').button('reset');
        });

        return false;
    }

    function createGameFromXml(event) {
        var form = $(this);
        form.find('button').button('loading');

        return true;
    }

    function onFileUploadComplete() {
        var span = $(this).contents().find('span');

        if (span.attr('success') === "true") {
            createAlertMsg('#errorMsg-createGame', true, "Game '" + span.text() + "' created successfully!");
            getGamesLists();
        }
        else {
            createAlertMsg('#errorMsg-createGame', false, span.text());
        }

        $('#createGameFromXml-form button').button('reset');
    }

    // Events
    $('#refreshBtn').on('click', getGamesLists);
    $('#createGame-form').on('submit', createGame);
    $('#createGameFromXml-form').on('submit', createGameFromXml);
    $("#upload_iframe").on('load', onFileUploadComplete);
    
    // Utils Functions
    function createAlertMsg(divId, isSuccess, msg) {
        var alertMsg = "<div class='alert";

        if (isSuccess === true) {
            alertMsg += " alert-success ";
        }
        else {
            alertMsg += " alert-danger ";
        }

        alertMsg += "fade in'>"
                + "<button type='button' class='close' data-dismiss='alert' aria-hidden='true'>×</button>"
                + "<p>" + msg + "</p>"
                + "</div>";

        $(divId).empty().append(alertMsg);
    }

    function createLoading(isSmall) {
        var result = '<div class="loading';

        if (isSmall === true) {
            result += " small";
        }

        result += '"><div></div><h5>Loading...</h5></div>';

        return result;
    }
});
