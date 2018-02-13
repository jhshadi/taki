package jhshadi.com.taki.services.exceptions;

public class InvalidPlayerException extends Exception {

    private static final String INVALID_PLAYER_ID_EXCEPTION = "Invalid player id!";

    @Override
    public String getMessage() {
        return INVALID_PLAYER_ID_EXCEPTION;
    }
}
