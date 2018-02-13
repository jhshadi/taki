package jhshadi.com.taki.modules.takiLogic;

import jhshadi.com.taki.modules.takiLogic.exceptions.NoMoreCardsToDrawException;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors;
import jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Deck implements Iterable<Card> {

    static final int NUMBER_OF_DECKS_IN_GAME = 2;
    static final int START_OF_THE_DECK_INDEX = 0;
    private static final int CARD_WASNT_FOUND = -1;
    private List<Card> deck;

    public Deck() {
        deck = new ArrayList<>();
    }

    public Deck(List<Card> list) {
        deck = list;
    }

    List<Card> getDeck() {
        return deck;
    }

    int size() {
        return deck.size();
    }

    boolean isDeckEmpty() {
        return deck.isEmpty();
    }

    void addCard(Card card) {
        deck.add(card);
    }

    void addCards(Deck cards) {
        deck.addAll(cards.deck);
    }

    Card drawCard() throws NoMoreCardsToDrawException, IndexOutOfBoundsException {
        return drawCard(START_OF_THE_DECK_INDEX);
    }

    Card drawCard(int index) throws NoMoreCardsToDrawException, IndexOutOfBoundsException {
        Card result = null;

        try {
            result = deck.remove(index);
        } catch (IndexOutOfBoundsException e) {
            if (deck.isEmpty() == true) {
                throw new NoMoreCardsToDrawException();
            } else {
                throw e;
            }
        }

        return result;
    }

    Deck drawCardsFromTopOfTheDeck(int numberOfCards) throws NoMoreCardsToDrawException, IndexOutOfBoundsException {
        Deck result = new Deck();

        for (int i = 0; i < numberOfCards; i++) {
            result.addCard(this.drawCard());
        }

        return result;
    }

    boolean removeCard(Card card) {
        return deck.remove(card);
    }
    
    void clearDeck() {
        deck.clear();
    }

    void shuffle() {
        Collections.shuffle(deck);
    }

    boolean contains(Card card) {
        return deck.contains(card);
    }

    void createStartingDeck() {
        deck.clear();
        for (int i = 0; i < NUMBER_OF_DECKS_IN_GAME; i++) {
            createBaseDeck();
        }

        this.shuffle();
    }

    private void createBaseDeck() {
        List<CardColors> validColors = CardColors.getColorsOnly();
        CardColors cardColor;

        for (CardTypes type : CardTypes.values()) {
            for (CardColors color : validColors) {

                if (CardTypes.haveColor(type) == true) {
                    cardColor = color;
                } else {
                    cardColor = CardColors.NONE;
                }

                this.addCard(new Card(type, cardColor));
            }
        }
    }

    @Override
    public Iterator<Card> iterator() {
        return deck.iterator();
    }

    public Card get(int Index) throws IndexOutOfBoundsException {
        return deck.get(Index);
    }
}