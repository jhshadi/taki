package jhshadi.com.taki.services.exceptions;

public class InvalidCardException extends Exception {

    private static final String INVALID_CARD_EXCEPTION = "Invalid card played!";

    @Override
    public String getMessage() {
        return INVALID_CARD_EXCEPTION;
    }
}
