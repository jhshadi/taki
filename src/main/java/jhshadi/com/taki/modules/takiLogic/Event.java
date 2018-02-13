package jhshadi.com.taki.modules.takiLogic;


import jhshadi.com.taki.modules.takiLogic.takiEnums.EventType;

import java.util.ArrayList;
import java.util.List;

public class Event {

    private int eventId;
    private EventType eventType;
    private String playerName;
    private final List<Card> cards;
    private int timeout;

    Event() {
        cards = new ArrayList<>();
    }

    public int getEventId() {
        return eventId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public String getPlayerName() {
        return playerName;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public static abstract class EventFactory {

        public static Event createGameStart(Card card) {
            Event event = new Event();

            event.eventType = EventType.GAME_START;
            event.playerName = null;
            event.cards.add(card);
            event.timeout = 0;

            return event;
        }

        public static Event createGameOver() {
            Event event = new Event();

            event.eventType = EventType.GAME_OVER;
            event.playerName = null;
            event.timeout = 0;

            return event;
        }

        public static Event createGameWinner(String playerName) {
            Event event = new Event();

            event.eventType = EventType.GAME_WINNER;
            event.playerName = playerName;
            event.timeout = 0;

            return event;
        }

        public static Event createPlayerResigned(String playerName) {
            Event event = new Event();

            event.eventType = EventType.PLAYER_RESIGNED;
            event.playerName = playerName;
            event.timeout = 0;

            return event;
        }

        public static Event createPlayerTurn(String playerName) {
            Event event = new Event();

            event.eventType = EventType.PLAYER_TURN;
            event.playerName = playerName;
            event.timeout = 0;

            return event;
        }

        public static Event createCardsThrown(List<Card> cards) {
            Event event = new Event();

            event.eventType = EventType.CARDS_THROWN;
            event.playerName = null;
            event.cards.addAll(cards);
            event.timeout = 0;

            return event;
        }
        
        public static Event createPromptPlayerToThrowCards(String playeName, int timer) {
            Event event = new Event();

            event.eventType = EventType.WAIT_FOR_PLAYER_TO_THROW_CARDS;
            event.playerName = playeName;
            event.timeout = 0;

            return event;
        }
        
        public static Event createCardsTakenFromStack() {
            Event event = new Event();

            event.eventType = EventType.CARDS_TAKEN_FROM_STACK;
            event.playerName = null;
            event.timeout = 0;

            return event;
        }
    }

}
