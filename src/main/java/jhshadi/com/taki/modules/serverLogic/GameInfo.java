package jhshadi.com.taki.modules.serverLogic;

import jhshadi.com.taki.modules.serverLogic.enums.GameState;
import jhshadi.com.taki.modules.serverLogic.exceptions.InitializeLogicException;
import jhshadi.com.taki.modules.takiLogic.*;
import jhshadi.com.taki.modules.takiLogic.exceptions.*;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class GameInfo {

    private static final int MAX_NUMBER_OF_PLAYERS = 4;
    private static final int MIN_NUMBER_OF_PLAYERS = 2;
    private static final int TIMER_TIMEOUT = 30 * 1000;
    // Data Members
    protected static OnFinishedGameInterface onFinishGame;
    protected String name;
    protected int computerizedPlayers;
    protected int humanPlayers;
    protected int joinedHumanPlayers;
    protected GameState gameState;
    protected TakiLogic logic;
    protected Timer timeoutTimer;
    protected List<Player> allPlayers;

    // Setters
    public static void setOnFinishGame(OnFinishedGameInterface onFinishGame) {
        GameInfo.onFinishGame = onFinishGame;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getComputerizedPlayers() {
        return computerizedPlayers;
    }

    public int getHumanPlayers() {
        return humanPlayers;
    }

    public int getJoinedHumanPlayers() {
        return joinedHumanPlayers;
    }

    public GameState getGameState() {
        return gameState;
    }

    public TakiLogic getLogic() {
        return logic;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    // C'tor
    GameInfo() {
        this.joinedHumanPlayers = 0;
        this.gameState = GameState.WAITING;
        this.timeoutTimer = new Timer();
        this.allPlayers = new ArrayList<>();
    }

    boolean validatePlayersNumbers() {
        return (MIN_NUMBER_OF_PLAYERS <= computerizedPlayers + humanPlayers && computerizedPlayers + humanPlayers <= MAX_NUMBER_OF_PLAYERS) && (0 < humanPlayers);
    }

    boolean validateJoinPlayer() {
        return joinedHumanPlayers < humanPlayers;
    }

    Player joinHumanPlayer(String playerName) throws ExistPlayerNameException, InvalidNumberOfPlayersInGame, InitializeLogicException {
        Player joinedPlayer;

        if (validateJoinPlayer() == false) {
            throw new InvalidNumberOfPlayersInGame();
        }

        joinedPlayer = joinHumanPlayerHelper(playerName);

        allPlayers.add(joinedPlayer);
        joinedHumanPlayers++;

        checkIfAllPlayersJoined();

        return joinedPlayer;
    }

    abstract Player joinHumanPlayerHelper(String playerName) throws ExistPlayerNameException, InvalidNumberOfPlayersInGame;

    void checkIfAllPlayersJoined() throws InitializeLogicException {
        if (joinedHumanPlayers == humanPlayers) {
            gameState = GameState.ACTIVE;

            initializeGameLogic();
            logic.getEventQueue().push(Event.EventFactory.createGameStart(logic.getCurrentCard()));
            setNextTurn();
        }
    }

    abstract void initializeGameLogic() throws InitializeLogicException;

    // GameFlow Functions
    public Player getPlayerById(int playerId) {
        Player result = null;

        for (Player player : allPlayers) {
            if (playerId == player.getId()) {
                result = player;
                break;
            }
        }

        return result;
    }

    public void throwCards(int playerId, List<Card> cards) throws InvalidCardPlayedException {
        List<Card> cardsThrown = new ArrayList<>();

        stopTimer();

        if (cards.isEmpty()) {
            try {
                logic.otherActionsInTurn();
                logic.getEventQueue().push(Event.EventFactory.createCardsTakenFromStack());
            } catch (NoMoreCardsToDrawException ex) {
                // Nothing to be done
            }
        } else {
            while (!logic.isTurnOver()) {
                if (cards.isEmpty()) {
                    try {
                        logic.otherActionsInTurn();
                    } catch (NoMoreCardsToDrawException ex) {
                        // Nothing to be done
                    }
                } else {
                    cardsThrown.add(throwCard(cards.get(0)));
                    cards.remove(0);
                }
            }

            logic.getEventQueue().push(Event.EventFactory.createCardsThrown(cardsThrown));
        }

        setNextTurn();
    }

    private Card throwCard(Card card) throws InvalidCardPlayedException {
        Card logicCard = card;

        if (logicCard.getType() == CardTypes.CHANGE_COLOR) {
            logicCard = new Card(CardTypes.CHANGE_COLOR, CardColors.NONE);
        }

        try {
            logic.playCardInTurn(logicCard);
        } catch (ChangeColorCardException ex) {
            try {
                logic.changeCurrentColor(card.getColor());
                logicCard = new Card(CardTypes.CHANGE_COLOR, card.getColor());
            } catch (ChangeColorCardException ex1) {
                startTimeoutCountdown();
                throw new InvalidCardPlayedException();
            }
        }

        return logicCard;
    }

    private void setNextTurn() {
        Player winner;

        while (!logic.getCurrentPlayer().isIsHuman() && !logic.isGameOver()) {
            logic.getEventQueue().push(Event.EventFactory.createPlayerTurn(logic.getCurrentPlayer().getName()));
            ((ComputerPlayer) logic.getCurrentPlayer()).aiPlayComputerPlayer(logic);
        }

        if (logic.isGameOver()) {
            try {
                winner = logic.getWinner();
                logic.getEventQueue().push(Event.EventFactory.createGameWinner(winner.getName()));
            } catch (NoWinnerException e) {
                // Nothing to be done
            } finally {
                logic.getEventQueue().push(Event.EventFactory.createGameOver());
                setGameToFinishList();
            }
        } else {
            logic.getEventQueue().push(Event.EventFactory.createPlayerTurn(logic.getCurrentPlayer().getName()));
            logic.getEventQueue().push(Event.EventFactory.createPromptPlayerToThrowCards(logic.getCurrentPlayer().getName(), TIMER_TIMEOUT));
            startTimeoutCountdown();

            if (logic.getCurrentPlayer().isResigned()) {
                resignPlayer(logic.getCurrentPlayer());
            }
        }
    }

    public void resignPlayer(Player player) {
        player.setResigned(true);

        if (player.getId() == logic.getCurrentPlayer().getId()) {
            stopTimer();
            logic.resign();
            logic.getEventQueue().push(Event.EventFactory.createPlayerResigned(player.getName()));
            setNextTurn();
        }
    }

    public void setGameToFinishList() {
        gameState = GameState.FINISHED;
        onFinishGame.moveGameToFinishList(name);
    }

    private void startTimeoutCountdown() {
        timeoutTimer = new Timer();
        timeoutTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (logic.getCurrentPlayer().isIsHuman() == true && logic.getCurrentPlayer().isResigned() == false) {
                    resignPlayer(logic.getCurrentPlayer());
                }
            }
        }, TIMER_TIMEOUT);
    }

    private void stopTimer() {
        timeoutTimer.cancel();
        timeoutTimer.purge();
    }
}
