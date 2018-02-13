package jhshadi.com.taki.services.vos.convertors;

import jhshadi.com.taki.modules.serverLogic.enums.GameState;
import jhshadi.com.taki.modules.takiLogic.Deck;
import jhshadi.com.taki.services.vos.*;

import java.util.ArrayList;
import java.util.List;

public class WsConvertor {

    // Player\Game Details
    public static GameStatus convertToGameStatusWs(GameState convert) {
        GameStatus res = null;

        switch (convert) {
            case ACTIVE:
                res = GameStatus.ACTIVE;
                break;
            case WAITING:
                res = GameStatus.WAITING;
                break;
            case FINISHED:
                res = GameStatus.FINISHED;
                break;
        }

        return res;
    }

    public static PlayerType convertToPlayerTypeWs(boolean isHuman) {
        PlayerType res;

        if (isHuman) {
            res = PlayerType.HUMAN;
        } else {
            res = PlayerType.COMPUTER;
        }

        return res;
    }

    public static PlayerStatus convertToPlayerStatusWs(boolean isResign) {
        PlayerStatus res;

        if (isResign) {
            res = PlayerStatus.RETIRED;
        } else {
            res = PlayerStatus.ACTIVE;
        }

        return res;
    }

    // Cards
    public static List<Card> convertToPlayerCardListWs(Deck deck) {
        List<Card> res = new ArrayList<>();

        for (jhshadi.com.taki.modules.takiLogic.Card card : deck) {
            res.add(convertToCardWs(card));
        }

        return res;
    }

    public static Card convertToCardWs(jhshadi.com.taki.modules.takiLogic.Card card) {
        Card res = new Card();

        res.setColor(convertToColorWs(card.getColor()));
        res.setType(convertToTypeWs(card.getType()));
        res.setValue(convertToValueWs(card.getType()));

        return res;
    }

    public static Color convertToColorWs(jhshadi.com.taki.modules.takiLogic.takiEnums.CardColors color) {
        Color res = null;

        switch (color) {
            case BLUE:
                res = Color.BLUE;
                break;
            case GREEN:
                res = Color.GREEN;
                break;
            case RED:
                res = Color.RED;
                break;
            case YELLOW:
                res = Color.YELLOW;
                break;
            case NONE:
                res = null;
                break;
        }

        return res;
    }

    public static Type convertToTypeWs(jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes type) {
        Type res = null;

        if (jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes.isNumber(type)) {
            res = Type.NUMBER;
        } else {
            switch (type) {
                case CHANGE_COLOR:
                    res = Type.CHANGE_COLOR;
                    break;
                case CHANGE_DIRECTION:
                    res = Type.CHANGE_DIRECTION;
                    break;
                case PLUS:
                    res = Type.PLUS;
                    break;
                case STOP:
                    res = Type.STOP;
                    break;
                case TAKI:
                    res = Type.TAKI;
                    break;
                case PLUS_2:
                    res = Type.NUMBER;
                    break;
            }
        }

        return res;
    }

    public static Value convertToValueWs(jhshadi.com.taki.modules.takiLogic.takiEnums.CardTypes value) {
        Value res = null;

        switch (value) {
            case ONE:
                res = Value.ONE;
                break;
            case PLUS_2:
                res = Value.TWO_PLUS;
                break;
            case THREE:
                res = Value.THREE;
                break;
            case FOUR:
                res = Value.FOUR;
                break;
            case FIVE:
                res = Value.FIVE;
                break;
            case SIX:
                res = Value.SIX;
                break;
            case SEVEN:
                res = Value.SEVEN;
                break;
            case EIGHT:
                res = Value.EIGHT;
                break;
            case NINE:
                res = Value.NINE;
                break;
        }

        return res;
    }

    // Events
    public static List<Event> convertToEventsListWs(List<jhshadi.com.taki.modules.takiLogic.Event> events) {
        List<Event> res = new ArrayList<>();

        for (jhshadi.com.taki.modules.takiLogic.Event event : events) {
            res.add(convertToEventWs(event));
        }

        return res;
    }

    public static Event convertToEventWs(jhshadi.com.taki.modules.takiLogic.Event event) {
        Event res = new Event();

        res.setId(event.getEventId());
        res.setPlayerName(event.getPlayerName());
        res.setTimeout(event.getTimeout());
        res.setType(convertToEventTypeWs(event.getEventType()));
        res.getCards().addAll(convertToPlayerCardListWs(new jhshadi.com.taki.modules.takiLogic.Deck(event.getCards())));

        return res;
    }

    public static EventType convertToEventTypeWs(jhshadi.com.taki.modules.takiLogic.takiEnums.EventType type) {
        EventType res = null;

        switch (type) {
            case CARDS_TAKEN_FROM_STACK:
                res = EventType.CARDS_TAKEN_FROM_STACK;
                break;
            case CARDS_THROWN:
                res = EventType.CARDS_THROWN;
                break;
            case GAME_OVER:
                res = EventType.GAME_OVER;
                break;
            case GAME_START:
                res = EventType.GAME_START;
                break;
            case GAME_WINNER:
                res = EventType.GAME_WINNER;
                break;
            case PLAYER_RESIGNED:
                res = EventType.PLAYER_RESIGNED;
                break;
            case PLAYER_TURN:
                res = EventType.PLAYER_TURN;
                break;
            case WAIT_FOR_PLAYER_TO_THROW_CARDS:
                res = EventType.WAIT_FOR_PLAYER_TO_THROW_CARDS;
                break;
        }

        return res;
    }
}
