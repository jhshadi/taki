
package jhshadi.com.taki.services.vos;

public enum EventType {

    GAME_START("GameStart"),
    GAME_OVER("GameOver"),
    GAME_WINNER("GameWinner"),
    PLAYER_RESIGNED("PlayerResigned"),
    PLAYER_TURN("PlayerTurn"),
    CARDS_THROWN("CardsThrown"),
    WAIT_FOR_PLAYER_TO_THROW_CARDS("WaitForPlayerToThrowCards"),
    CARDS_TAKEN_FROM_STACK("CardsTakenFromStack");
    private final String value;

    EventType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EventType fromValue(String v) {
        for (EventType c: EventType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
