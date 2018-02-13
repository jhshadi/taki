package jhshadi.com.taki.modules.takiLogic;


import jhshadi.com.taki.modules.takiLogic.exceptions.NoMoreCardsToDrawException;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes;

class CardManager {

    private Deck newCardsDeck;
    private Deck usedCardsDeck;
    private Card currentCard;

    public CardManager() {
        newCardsDeck = new Deck();
        usedCardsDeck = new Deck();
    }
    
    public void createStartingDeckInNewCardsDeck() {
        newCardsDeck.createStartingDeck();
    }

    public Deck getNewCardsDeck() {
        return newCardsDeck;
    }

    public Deck getUsedCardsDeck() {
        return usedCardsDeck;
    }

    public Card getCurrentCard() {
        return currentCard;
    }

    public void setCurrentCard(Card newCurrentCard) {
        if (currentCard != null) {
            this.usedCardsDeck.addCard(currentCard);
        }

        this.currentCard = newCurrentCard;
    }

    public void setCurrentCardFromNewCardsDeck() throws NoMoreCardsToDrawException, IndexOutOfBoundsException {
        int deckIndex = 0;

        for (Card card : newCardsDeck) {
            if (CardTypes.isNumber(card.getType()) == true) {
                break;
            }

            deckIndex++;
        }

        setCurrentCard(drawNewCard(deckIndex));
    }

    public Card drawNewCard() throws NoMoreCardsToDrawException, IndexOutOfBoundsException {
        return drawNewCard(Deck.START_OF_THE_DECK_INDEX);
    }

    private Card drawNewCard(int index) throws NoMoreCardsToDrawException, IndexOutOfBoundsException {
        try {
            return newCardsDeck.drawCard(index);
        } catch (NoMoreCardsToDrawException e) {
            if (usedCardsDeck.isDeckEmpty() == true) {
                throw new NoMoreCardsToDrawException();
            } else {
                swapNewCardsDeckWithUsedCardsDeck();
                newCardsDeck.shuffle();
                return newCardsDeck.drawCard();
            }
        }
    }

    public Deck drawCardsFromNewCardsDeck(int numberOfCards) throws NoMoreCardsToDrawException, IndexOutOfBoundsException {
        Deck result = new Deck();

        result.addCards(newCardsDeck.drawCardsFromTopOfTheDeck(numberOfCards));

        return result;
    }

    public void addCardsToNewCardsDeck(Deck cards) {
        newCardsDeck.addCards(cards);
        newCardsDeck.shuffle();
    }

    public void addCardsToUsedCardsDeck(Deck cards) {
        newCardsDeck.addCards(cards);
    }

    private void swapNewCardsDeckWithUsedCardsDeck() {
        Deck temp = usedCardsDeck;
        usedCardsDeck = newCardsDeck;
        newCardsDeck = temp;
    }
}