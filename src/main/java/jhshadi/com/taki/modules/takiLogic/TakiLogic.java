package jhshadi.com.taki.modules.takiLogic;

import jhshadi.com.taki.modules.takiLogic.exceptions.*;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes;

import java.util.ArrayList;
import java.util.List;

public class TakiLogic {

    // Consts
    public static final int MAX_NUMBER_OF_PLAYERS = 4;
    public static final int MIN_NUMBER_OF_PLAYERS = 2;
    public static final int PLUS_2_DRAW_CARDS_NOT_ACTIVE = 0;
    static final int NO_WINNER = -1;
    private static final int STARTING_PLAYER_INDEX = 0;
    private static final int STARTING_NUMBER_OF_CARDS_PER_PLAYER = 8;
    private static final int PLUS_2_DRAW_CARDS_MODIFIER = 2;
    private static final int COMPUTER_NAME_COUNTER_SET = 0;
    // Data Members
    private String gameName;
    private CardManager cardManager;
    private List<Player> players;
    private EventQueue eventQueue;
    private int currentPlayer;
    private int winnerPlayer;
    private boolean clockWise;
    private int plus2Modifier;
    private CardColors currentColor;
    private boolean takiCardStrike;
    private boolean isChangeColorFunctionAllowed;
    private boolean gameValid;
    private boolean gameOver;
    private boolean turnOver;

    //AddedByNeta for debug only
    public void printPlayres() {
        for (int i = 0; i < players.size(); i++) {
            System.out.print(players.get(i).getName());
        }
    }

    // C'tor
    public TakiLogic() {
    }

    // Setters
    void setCardManager(CardManager cardManager) {
        this.cardManager = cardManager;
    }

    void setPlayers(List<Player> players) {
        this.players = players;
    }

    void setEventQueue(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    void setWinnerPlayer(int winnerPlayer) {
        this.winnerPlayer = winnerPlayer;
    }

    void setIsClockWise(boolean isClockWise) {
        this.clockWise = isClockWise;
    }

    void setPlus2Modifier(int plus2Modifier) {
        this.plus2Modifier = plus2Modifier;
    }

    void setTakiCardStrike(boolean takiCardStrike) {
        this.takiCardStrike = takiCardStrike;
    }

    void setIsChangeColorFunctionAllowed(boolean isChangeColorFunctionAllowed) {
        this.isChangeColorFunctionAllowed = isChangeColorFunctionAllowed;
    }

    void setGameValid(boolean gameValid) {
        this.gameValid = gameValid;
    }

    void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    void setTurnOver(boolean turnOver) {
        this.turnOver = turnOver;
    }

    void setCurrentColor(CardColors currentColor) {
        this.currentColor = currentColor;
    }

    void setGameName(String gameName) {
        this.gameName = gameName;
    }

    // Getters
    public String getGameName() {
        return gameName;
    }

    CardManager getCardManager() {
        return cardManager;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<String> getPlayersNames() {
        List<String> playerNames = new ArrayList<>();

        for (Player player : players) {
            playerNames.add(player.getName());
        }

        return playerNames;
    }

    boolean isClockWise() {
        return clockWise;
    }

    public EventQueue getEventQueue() {
        return eventQueue;
    }

    public Card getCurrentCard() {
        return cardManager.getCurrentCard();
    }

    public CardColors getCurrentColor() {
        return currentColor;
    }

    public int getPlus2Modifier() {
        return plus2Modifier;
    }

    public boolean isTakiCardStrikeOn() {
        return takiCardStrike;
    }

    public boolean isGameValid() {
        return gameValid;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isTurnOver() {
        return turnOver;
    }

    // Game Flow Functions
    void setToDefaultTakiLogic() {
        this.cardManager = new CardManager();
        this.cardManager.createStartingDeckInNewCardsDeck();
        this.players = new ArrayList<>();
        this.eventQueue = new EventQueue();

        this.currentPlayer = STARTING_PLAYER_INDEX;
        this.currentColor = CardColors.NONE;
        this.clockWise = true;
        this.plus2Modifier = PLUS_2_DRAW_CARDS_NOT_ACTIVE;
        this.winnerPlayer = NO_WINNER;
        this.takiCardStrike = false;
        this.isChangeColorFunctionAllowed = false;
        this.gameValid = false;
        this.gameOver = false;
        this.turnOver = false;

        ComputerPlayer.setComputerCount(COMPUTER_NAME_COUNTER_SET);
    }

    public void addPlayer(Player player) throws InvalidNumberOfPlayersInGame, ExistPlayerNameException {
        if (MAX_NUMBER_OF_PLAYERS < players.size()) {
            throw new InvalidNumberOfPlayersInGame();
        } else {
            if (checkIfPlayerNameExist(player.getName()) == true) {
                throw new ExistPlayerNameException();
            }

            players.add(player);
        }
    }

    private boolean checkIfPlayerNameExist(String name) {
        boolean result = false;

        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(name) == true) {
                result = true;
                break;
            }
        }

        return result;
    }

    private void removePlayer(Player player) {
        cardManager.addCardsToNewCardsDeck(player.getDeck());
        players.remove(player);
        this.currentPlayer--;

        if (isValidNumberOfPlayres() == false) {
            gameOver = true;
        }
    }

    public void initializeGame() throws InvalidNumberOfPlayersInGame, NoMoreCardsToDrawException {
        if (isValidNumberOfPlayres() == true) {
            for (Player player : players) {
                player.getDeck().addCards(cardManager.drawCardsFromNewCardsDeck(STARTING_NUMBER_OF_CARDS_PER_PLAYER));
            }

            cardManager.setCurrentCardFromNewCardsDeck();
            currentColor = cardManager.getCurrentCard().getColor();
        } else {
            throw new InvalidNumberOfPlayersInGame();
        }

        this.gameValid = true;
    }

    private boolean isValidNumberOfPlayres() {
        return MIN_NUMBER_OF_PLAYERS <= players.size() && players.size() <= MAX_NUMBER_OF_PLAYERS;
    }

    // Get current player functions
    public Player getCurrentPlayer() {

        if (this.isTurnOver() == true) {
            setNextCurrentPlayer();
            turnOver = false;
        }

        return players.get(currentPlayer);
    }

    private void setNextCurrentPlayer() {
        int index = players.size();

        if (clockWise == true) {
            index++;
        } else {
            index--;
        }

        currentPlayer = (currentPlayer + index) % players.size();
    }

    // Play turn with card
    public void playCardInTurn(Card newCard) throws InvalidCardPlayedException, ChangeColorCardException {
        Player player = players.get(currentPlayer);

        if (Validator.validateCard(cardManager.getCurrentCard(), currentColor, plus2Modifier, takiCardStrike, newCard) == true) {
            setNewCurrentCardFromPlayer(newCard);
            checkIfPlayerWon();
            if (this.takiCardStrike == false) {
                this.turnOver = true;
                analyzeCardAndStartCardLogic(newCard);
            }

            // %E - eventQueue.push(new Event(player.getName(), newCard));
        } else {
            throw new InvalidCardPlayedException();
        }
    }

    private void setNewCurrentCardFromPlayer(Card card) {
        this.players.get(currentPlayer).getDeck().removeCard(card);
        this.cardManager.setCurrentCard(card);
        if (card.getType() != CardTypes.CHANGE_COLOR) {
            this.currentColor = card.getColor();
        }
    }

    private void checkIfPlayerWon() {
        if (players.get(currentPlayer).hasPlayerWon() == true) {
            winnerPlayer = currentPlayer;
            gameOver = true;
        }
    }

    private void analyzeCardAndStartCardLogic(Card card) throws ChangeColorCardException {
        if (CardTypes.isNumber(card.getType()) == true) {
            numberCardLogic();
        } else {
            switch (card.getType()) {
                case PLUS:
                    plusCardLogic();
                    break;
                case PLUS_2:
                    plus2CardLogic();
                    break;
                case STOP:
                    stopCardLogic();
                    break;
                case CHANGE_DIRECTION:
                    switchDirectionCardLogic();
                    break;
                case TAKI:
                    takiCardLogic();
                    break;
                case CHANGE_COLOR:
                    changeColorCardLogic();
            }
        }
    }

    // Card Logic Function
    private void numberCardLogic() {
        /* No need for more actions */
    }

    private void plusCardLogic() {
        this.turnOver = false;
    }

    private void plus2CardLogic() {
        this.plus2Modifier += PLUS_2_DRAW_CARDS_MODIFIER;
    }

    private void switchDirectionCardLogic() {
        this.clockWise = !clockWise;
    }

    private void stopCardLogic() {
        this.setNextCurrentPlayer();
    }

    private void takiCardLogic() {
        if (this.takiCardStrike == false) {
            this.turnOver = false;
            this.takiCardStrike = true;
        }
    }

    // Change Color Card Logic
    private void changeColorCardLogic() throws ChangeColorCardException {
        this.isChangeColorFunctionAllowed = true;
        throw new ChangeColorCardException(getValidColorsToChangeTo());
    }

    public void changeCurrentColor(CardColors color) throws ChangeColorCardException {
        if (isChangeColorFunctionAllowed == true && color != currentColor) {
            this.isChangeColorFunctionAllowed = false;
            this.currentColor = color;
            // %E - eventQueue.push(new Event(players.get(currentPlayer).getName(), cardManager.getCurrentCard(), color));
        } else {
            throw new ChangeColorCardException(getValidColorsToChangeTo());
        }
    }

    private List<CardColors> getValidColorsToChangeTo() {
        List<CardColors> colors = CardColors.getColorsOnly();
        colors.remove(currentColor);
        return colors;
    }

    // Other Action in turn (Close TAKI & Draw cards)
    public void otherActionsInTurn() throws NoMoreCardsToDrawException {
        Player player = players.get(currentPlayer);

        this.turnOver = true;
        if (this.takiCardStrike == true) {
            closeTaki(player);
        } else {
            takeCardsFromNewCardsDeck(player);
        }
    }

    private void closeTaki(Player player) {
        try {
            analyzeCardAndStartCardLogic(cardManager.getCurrentCard());
        } catch (ChangeColorCardException e) {
            gameOver = true;
        } finally {
            this.takiCardStrike = false;
            // %E - this.eventQueue.push(new Event(player.getName(), Event.CLOSE_TAKI));
        }
    }

    private void takeCardsFromNewCardsDeck(Player player) throws NoMoreCardsToDrawException {
        try {
            if (this.plus2Modifier == PLUS_2_DRAW_CARDS_NOT_ACTIVE) {
                player.getDeck().addCard(cardManager.drawNewCard());
                // %E - this.eventQueue.push(new Event(player.getName(), Event.DRAW_CARD));
            } else {
                player.getDeck().addCards(cardManager.drawCardsFromNewCardsDeck(this.plus2Modifier));
                // %E - this.eventQueue.push(new Event(player.getName(), plus2Modifier));
            }
        } catch (NoMoreCardsToDrawException e) {
            // %E - eventQueue.push(new Event(player.getName(), Event.NO_CARD_DRAWN));
            throw e;
        } finally {
            if (this.plus2Modifier != PLUS_2_DRAW_CARDS_NOT_ACTIVE) {
                this.plus2Modifier = PLUS_2_DRAW_CARDS_NOT_ACTIVE;
            }
        }
    }

    // End Game Functions
    public void resign() {
        Player player = players.get(currentPlayer);

        removePlayer(player);
        if (this.takiCardStrike == true) {
            this.closeTaki(player);
        }
        this.turnOver = true;

        if (checkIfThereisHumanPlayerLeft() == false) {
            gameOver = true;
        }
    }

    private boolean checkIfThereisHumanPlayerLeft() {
        boolean result = false;

        for (Player player : players) {
            if (player.isIsHuman() == true) {
                result = true;
                break;
            }
        }

        return result;
    }

    public void quit() {
        this.gameOver = true;
    }

    public Player getWinner() throws NoWinnerException {
        Player winner;

        if (winnerPlayer != NO_WINNER) {
            winner = players.get(winnerPlayer);
        } else {
            throw new NoWinnerException();
        }

        return winner;
    }

    public static abstract class LogicFactory {

        public static TakiLogic create() {
            TakiLogic newTakiLogic = new TakiLogic();

            newTakiLogic.setToDefaultTakiLogic();

            return newTakiLogic;
        }
    }
}
