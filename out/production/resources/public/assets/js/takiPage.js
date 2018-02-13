$(document).on("ready", function() {

    // Consts
    var POLLING_RATE = 2500;
    var PLUS_2_DRAW_CARDS_MODIFIER = 2;
    var RESIGN_PLAYER_UI_OPACITY = 0.5;
    var AJAX_ERROR = 'Unable to get ajax response, Game Abort!';

    // Game Logic Variable
    var eventsQueue = new Array();
    var currentCard = null;
    var plus2Modifier = 0;
    var takiCardStrike = false;
    var currentPlayer = null;
    var cardsPlayed = new Array();
    var winner = null;
    var isGameOver = false;
    var isGameStart = false;

    // Initialize
    initialize();
    function initialize() {
        setScroller();
        setResignButtonOnOff(true);
        triggerAjaxPollingEvents();
    }

    function setScroller() {
        $("#playerDeck").niceScroll({
            cursorcolor: "#f5f5f5",
            cursoropacitymin: "1",
            cursoropacitymax: "1",
            cursorwidth: "10px",
            cursorborder: "1px solid #dddddd",
            background: "#ffffff",
            touchbehavior: "true",
            //railoffset: "true",
            //sensitiverail: "true"
        });
    }

    function showTakiBoard() {
        $('#loadingBox').remove();
        $('body').css('overflow', 'auto');
        $('#taki-board').css('visibility', 'visible');
    }

    // Functions
    // Polling
    function triggerAjaxPollingEvents() {
        setTimeout(ajaxPollingEvents, POLLING_RATE);
    }
    function ajaxPollingEvents() {
        $.ajax({
            url: "/GetEvents",
            dataType: 'json',
            cache: false,
            success: function(data) {
                if (data.error == null) {
                    eventsQueue.push.apply(eventsQueue, data.events);
                    drawEvent();

                    if (isGameOver === false) {
                        triggerAjaxPollingEvents();
                    }
                }
                else {
                    if (isGameStart === false) {
                        showTakiBoard();
                        $('#current-player .playerUI').css('visibility', 'hidden');
                    }

                    isGameOver = true;
                    setPlayerUIOff();
                    setResignButtonOnOff(true);
                    showMsgBox("Game Error", AJAX_ERROR, true);
                }
            },
            error: function(error) {
                if (isGameOver === false) {
                    triggerAjaxPollingEvents();
                }
            }
        });
    }

    // Handle Events Functions
    function drawEvent() {
        var event;

        if (0 < eventsQueue.length) {
            event = eventsQueue[0];
            eventsQueue.splice(0, 1);

            switch (event.type) {
                case "GAME_START":
                    gameStartEvent(event);
                    break;
                case "GAME_OVER":
                    gameOverEvent(event);
                    break;
                case "GAME_WINNER":
                    gameWinnerEvent(event);
                    break;
                case "PLAYER_RESIGNED":
                    playerResignedEvent(event);
                    break;
                case "PLAYER_TURN":
                    playerTurnEvent(event);
                    break;
                case "CARDS_THROWN":
                    cardsThrownEvent(event);
                    break;
                case "WAIT_FOR_PLAYER_TO_THROW_CARDS":
                    waitForPlayerToThrowCards(event);
                    break;
                case "CARDS_TAKEN_FROM_STACK":
                    cardsTakenFromStack(event);
                    break;
            }
        }
    }

    // Handle Event Types Functions
    function gameStartEvent(event) {
        showTakiBoard();
        isGameStart = true;

        writeMsgToLog("Game Start");
        setResignButtonOnOff(false);

        updateCardsToDrawFromStack();
        setCurrentCard(event.cards[0]);

        writeMsgToLog("Opening card: " + getCardAsString(event.cards[0]));

        setPlayerUIOff();

        initializePlayers();
    }
    function gameOverEvent(event) {
        writeMsgToLog("Game Over!");
        isGameOver = true;

        var winnerString = getWinnerString();
        showMsgBox("Game Over", winnerString, false);
    }
    function gameWinnerEvent(event) {
        winner = event.playerName;

        writeMsgToLog(getWinnerString());

        drawEvent();
    }
    function playerResignedEvent(event) {
        writeMsgToLog(event.playerName + " resigned from the game.");

        var playerName = $('#current-player h4').text();

        if (event.playerName === playerName) {
            isGameOver = true;
            setPlayerUIOff();
            setResignButtonOnOff(true);
            showMsgBox("Player Resigned", "Player Resigned due to timeout!", false);
        } else {
            $(getPlayerUIByPlayerName(event.playerName)).css('opacity', RESIGN_PLAYER_UI_OPACITY);
        }

        drawEvent();
    }
    function playerTurnEvent(event) {
        writeMsgToLog(event.playerName + " is turn to play.");

        currentPlayer = event.playerName;

        drawEvent();
    }
    function cardsThrownEvent(event) {
        var cards = event.cards;

        $.each(cards || [], function(i, card) {
            writeMsgToLog(currentPlayer + " throw the card: " + getCardAsString(card));
            setCurrentCard(card);
        });

        if ((cards[cards.length - 1]).type === "NUMBER" && (cards[cards.length - 1]).value === "TWO_PLUS") {
            plus2Modifier += PLUS_2_DRAW_CARDS_MODIFIER;
        } else {
            plus2Modifier = 0;
        }

        updateCardsToDrawFromStack();

        drawEvent();
    }
    function waitForPlayerToThrowCards(event) {
        var playerName = $('#current-player h4').text();

        if (event.playerName === playerName) {
            $.ajax({
                url: '/GetPlayersDetails',
                dataType: 'json',
                cache: false,
                success: function(data) {
                    var currentPlayer = data.currentPlayerDetails;

                    updateCurrentPlayerDeck(currentPlayer.cards);
                    setPlayerUIOn();

                    drawEvent();
                },
                error: function(error) {
                    isGameOver = true;
                    showMsgBox("Game Error", AJAX_ERROR, true);
                }
            });
        }
    }
    function cardsTakenFromStack(event) {
        writeMsgToLog(currentPlayer + " Draw Card\s.");
        plus2Modifier = 0;
        updateCardsToDrawFromStack();

        drawEvent();
    }

    // Initialize Game Functions
    function initializePlayers() {
        $.ajax({
            url: '/GetPlayersDetails',
            dataType: 'json',
            cache: false,
            success: function(data) {
                var players = data.playersDetails;
                var currentPlayer = data.currentPlayerDetails;

                setPlayersToBoard(players);
                setCurrentPlayerToBoard(currentPlayer);

                drawEvent();
            },
            error: function(error) {
                isGameOver = true;
                showMsgBox("Game Error", AJAX_ERROR, true);
            }
        });
    }
    function setPlayersToBoard(players) {

        $.each(players || [], function(i, player) {
            switch (i) {
                case 0:
                    if (players.length < 2) {
                        $('#player-top').append(createPlayerUI(player.name));
                    } else {
                        $('#player-left').append(createPlayerUI(player.name));
                    }
                    break;
                case 1:
                    $('#player-top').append(createPlayerUI(player.name));
                    break;
                case 2:
                    $('#player-right').append(createPlayerUI(player.name));
                    break;
            }
        });
    }
    function createPlayerUI(playerName) {
        return '<div class="playerUI"><h4>' + playerName + '</h4><div class="card back"></div></div>';
    }
    function setCurrentPlayerToBoard(currentPlayer) {
        $('#current-player h4').text(currentPlayer.name);

        updateCurrentPlayerDeck(currentPlayer.cards);
    }
    function updateCurrentPlayerDeck(cards) {
        var playerDeck = $('#playerDeck');

        playerDeck.empty();

        $.each(cards || [], function(i, card) {
            playerDeck.append(createCardFromJSON(card));
        });
    }
    function getPlayerUIByPlayerName(playerName) {
        var playersUI = $('div.playerUI h4');
        var result;

        $.each(playersUI || [], function(i, playerUI) {
            if ($(playerUI).text() === playerName) {
                result = playerUI;
                return false;
            }
        });

        return result;
    }

    // Turn on/off player UI Functions
    function setPlayerUIOn() {
        var stack = $('#stack');
        var currentPlayerPanel = $('#current-player');

        enableBoardEvents();

        stack.css('opacity', '1.0');
        currentPlayerPanel.css('opacity', '1.0');

        $("#playerDeck").getNiceScroll().resize();
    }
    function setPlayerUIOff() {
        var stack = $('#stack');
        var currentPlayerPanel = $('#current-player');

        disableBoardEvent();

        stack.css('opacity', RESIGN_PLAYER_UI_OPACITY);
        currentPlayerPanel.css('opacity', RESIGN_PLAYER_UI_OPACITY);
    }
    function enableBoardEvents() {
        var playerDeck = $("#playerDeck div.card");
        var stack = $('#stack');

        playerDeck.swipe({
            tap: function(event, target) {
                onCardMouseClick($(event.currentTarget));
            }
        }).addClass('clickable');

        stack.on('click', onStackMouseClick);
        stack.children('div.card').addClass('clickable');
    }
    function disableBoardEvent() {
        var playerDeck = $("#playerDeck div.card");
        var stack = $('#stack');

        playerDeck.each(function(i, card) {
            $(card).swipe('destroy').removeClass('clickable');
        });
        stack.off('click');
        stack.children('div.card').removeClass('clickable');
    }
    function setResignButtonOnOff(isDisabled) {
        if (isDisabled === true) {
            $('#resign-button').attr('disabled', 'disabled');
            $('#resign-button').off('click');
        }
        else {
            $('#resign-button').removeAttr('disabled');
            $('#resign-button').on('click', resign);
        }
    }

    function updateStackAndPile(card) {
        setCurrentCard(card);
        updateCardsToDrawFromStack();
    }
    function setCurrentCard(card) {
        var pile = $('#pile');

        currentCard = card;

        pile.empty();
        pile.append(createCardFromJSON(currentCard));
    }
    function updateCardsToDrawFromStack() {
        var stackText = $('#stackText');

        if (takiCardStrike === true) {
            stackText.text("Close Taki");
        } else {
            if (0 < plus2Modifier) {
                stackText.text("Draw " + plus2Modifier + " Cards");
            } else {
                stackText.text("Draw Card");
            }
        }
    }
    function writeMsgToLog(msg) {
        var log = $('#log');
        log.val(msg + '\n' + log.val()).scrollTop();
    }
    function getWinnerString() {
        var msg;

        if (winner === null) {
            msg = "There is no Winner!";
        } else {
            msg = winner + " is the Winner!";
        }

        return msg;
    }

    // Player Turn Actions Functions
    function onCardMouseClick(jqueryCard) {
        disableBoardEvent();

        var card = getCardAsJSON(jqueryCard);

        if (validateCard(currentCard, currentCard.color, plus2Modifier, takiCardStrike, card) === true) {

            removeCardFromPlayerDeck(jqueryCard);

            if (card.type === "CHANGE_COLOR") {
                showChangeColorDialog();
            } else {

                if (card.type === "TAKI") {
                    takiCardStrike = true;
                }

                updateStackAndPile(card);
                cardsPlayed.push(card);

                if (takiCardStrike === false && card.type !== "PLUS") {
                    setPlayerUIOff();
                    throwCards();
                }
                else {
                    enableBoardEvents();
                }
            }
        } else {
            enableBoardEvents();
            setCardInvalid(jqueryCard);
        }
    }
    function onStackMouseClick() {
        setPlayerUIOff();

        takiCardStrike = false;
        throwCards();
    }
    function setCardInvalid(jqueryCard) {
        jqueryCard.swipe("destroy").removeClass('clickable');

        jqueryCard.addClass('invalid').children('div').fadeTo('slow', 0, function() {
            jqueryCard.removeClass('invalid').swipe({
                tap: function(event, target) {
                    onCardMouseClick($(event.currentTarget));
                }
            }).addClass('clickable');
            $(this).removeAttr('style');
        });
    }
    function removeCardFromPlayerDeck(jqueryCard) {
        jqueryCard.remove();
    }

    function throwCards() {

        $.ajax({
            url: "/ThrowCards",
            type: "POST",
            cache: false,
            data: {cards: JSON.stringify(cardsPlayed)},
            success: function(data) {
                if (data.error == null) {
                    cardsPlayed = new Array();
                }
                else {
                    isGameOver = true;
                    showMsgBox("Game Error", data.error, true);
                }
            },
            error: function(error) {
                isGameOver = true;
                showMsgBox("Game Error", AJAX_ERROR, true);
            }
        });
    }

    // Change Color Box
    function showChangeColorDialog() {
        setPlayerUIOff();
        var changeColorDialog = $('#changeColorBox');
        var changeColorDialogColors = changeColorDialog.children('#changeColorBox-colors');
        var stackAndPile = $('#stack-and-pile');

        var validColors = getValidColors();

        changeColorDialogColors.empty();
        $.each(validColors || [], function(i, color) {
            changeColorDialogColors.append('<div rectcolor="' + color + '" class="changeColorRect clickable ' + color + '"></div>');
        });

        changeColorDialogColors.children('div.changeColorRect').on('click', onChangeColorBoxRectClick);

        stackAndPile.css('display', 'none');
        changeColorDialog.css('display', 'inline-block');
    }
    function hideChangeColorDialog() {
        $('#changeColorBox').css('display', 'none');
        $('#stack-and-pile').css('display', 'inline-block');
        setPlayerUIOn();
    }
    function getValidColors() {
        var validColors = ['RED', 'YELLOW', 'GREEN', 'BLUE'];
        var currentColor = currentCard.color;

        $.each(validColors || [], function(i, color) {
            if (color === currentColor) {
                validColors.splice(i, 1);
                return false;
            }
        });

        return validColors;
    }
    function onChangeColorBoxRectClick(event) {
        var card = new Object();
        card.type = "CHANGE_COLOR";
        card.color = $(this).attr('rectcolor');

        updateStackAndPile(card);
        cardsPlayed.push(card);
        hideChangeColorDialog();
        throwCards();
    }

    // Resign
    function resign() {
        $.ajax({
            url: "/Resign",
            dataType: 'json',
            cache: false,
            success: function(data) {
                if (data.error == null) {
                    showMsgBox("Player Resigned", "", false);
                }
                else {
                    showMsgBox("Game Error", data.error, true);
                }
            },
            error: function(error) {
                showMsgBox("Game Error", AJAX_ERROR, true);
            }
        });

        isGameOver = true;
        setPlayerUIOff();
        setResignButtonOnOff(true);
    }

    // Convert Card Functions
    function createCardFromJSON(card) {
        var cardImage = "";
        var result = '<div';

        if (card.type !== undefined) {
            result += ' cardType="' + card.type + '"';
            cardImage += card.type;
        }
        if (card.color !== undefined) {
            result += ' cardColor="' + card.color + '"';
            cardImage += '-' + card.color;
        }
        if (card.value !== undefined) {
            result += ' cardValue="' + card.value + '"';
            cardImage += '-' + card.value;
        }

        result += ' class="card " style="background-image: url(assets/img/cards/'
                + cardImage + '.png);"><div></div></div>';

        return result;
    }
    function getCardAsJSON(jqueryCard) {
        var card = new Object();
        var cardType = jqueryCard.attr('cardType');
        var cardColor = jqueryCard.attr('cardColor');
        var cardValue = jqueryCard.attr('cardValue');

        if (cardType !== undefined) {
            card.type = cardType;
        }
        if (cardColor !== undefined) {
            card.color = cardColor;
        }
        if (cardValue !== undefined) {
            card.value = cardValue;
        }

        return card;
    }
    function getCardAsString(card) {
        var strCard = "";

        if (card.type === "NUMBER") {
            switch (card.value) {
                case "ONE":
                    strCard += "1";
                    break;
                case "TWO_PLUS":
                    strCard += "+2";
                    break;
                case "THREE":
                    strCard += "3";
                    break;
                case "FOUR":
                    strCard += "4";
                    break;
                case "FIVE":
                    strCard += "5";
                    break;
                case "SIX":
                    strCard += "6";
                    break;
                case "SEVEN":
                    strCard += "7";
                    break;
                case "EIGHT":
                    strCard += "8";
                    break;
                case "NINE":
                    strCard += "9";
                    break;
            }
        }
        else {
            switch (card.type) {
                case "PLUS":
                    strCard += "+";
                    break;
                case "STOP":
                    strCard += "Stop";
                    break;
                case "CHANGE_DIRECTION":
                    strCard += "Change Direction";
                    break;
                case "CHANGE_COLOR":
                    strCard += "Change Color";
                    break;
                case "TAKI":
                    strCard += "TAKI";
                    break;
            }
        }

        if (card.color !== undefined) {
            strCard += ' (';

            switch (card.color) {
                case "BLUE":
                    strCard += "BLUE";
                    break;
                case "GREEN":
                    strCard += "GREEN";
                    break;
                case "YELLOW":
                    strCard += "YELLOW";
                    break;
                case "RED":
                    strCard += "RED";
                    break;
            }

            strCard += ')';
        }

        return strCard;
    }

    // Validator
    function validateCard(currentCard, currentColor, plus2Modifier, isTakiStrikeOn, newCard) {

        var validateOpenTakiMode = function(currentCard, newCard, currentColor) {
            return validateCardColor(currentColor, newCard) || validateCardType(currentCard, newCard);
        };

        var validatePlus2Mode = function(newCard) {
            return newCard.type === "NUMBER" && newCard.value === "TWO_PLUS";
        };

        var validateChangeColor = function(currentCard) {
            return currentCard.type !== "CHANGE_COLOR";
        };

        var validateCardColor = function(currentColor, newCard) {
            return newCard.color === currentColor;
        };

        var validateCardType = function(currentCard, newCard) {
            var result = false;

            if (newCard.type === currentCard.type) {
                if (newCard.type === "NUMBER") {
                    if (newCard.value === currentCard.value) {
                        result = true;
                    }
                }
                else {
                    result = true;
                }
            }

            return result;
        };

        var PLUS_2_DRAW_CARDS_NOT_ACTIVE = 0;
        var isValid = false;

        if (isTakiStrikeOn === true) {
            isValid = validateOpenTakiMode(currentCard, newCard, currentColor);
        } else if (plus2Modifier !== PLUS_2_DRAW_CARDS_NOT_ACTIVE) {
            isValid = validatePlus2Mode(newCard);
        } else if (newCard.type === "CHANGE_COLOR") {
            isValid = validateChangeColor(currentCard);
        } else if (validateCardType(currentCard, newCard) === true) {
            isValid = true;
        } else if (validateCardColor(currentColor, newCard) === true) {
            isValid = true;
        }

        return isValid;
    }

    // MsgBox Functions
    function showMsgBox(title, msg, isError) {
        setPlayerUIOff();
        setResignButtonOnOff(true);

        var msgBox = $('#msgBox');
        var msgBoxBtn = $('#msgBox #msgBox-btn');

        msgBox.removeClass('panel-default').removeClass('panel-danger');
        msgBoxBtn.removeClass('btn-primary').removeClass('btn-danger');

        if (isError === true) {
            msgBox.addClass('panel-danger');
            msgBoxBtn.addClass('btn-danger');
        }
        else {
            msgBox.addClass('panel-default');
            msgBoxBtn.addClass('btn-primary');
        }

        msgBox.find('#msgBox-title').text(title);
        msgBox.find('#msgBox-text').text(msg);
        msgBox.find('#msgBox-btn').on('click', onMsgBoxBtnClick);

        $('#stack-and-pile').css('display', 'none');
        msgBox.css('display', 'inline-block');
    }
    function onMsgBoxBtnClick(event) {
        window.location.replace('/index.html');
    }
});