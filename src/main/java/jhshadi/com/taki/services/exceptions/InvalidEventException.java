package jhshadi.com.taki.services.exceptions;

public class InvalidEventException extends Exception {

    private static final String INVALID_EVENT_ID_EXCEPTION = "Invalid event id!";

    @Override
    public String getMessage() {
        return INVALID_EVENT_ID_EXCEPTION;
    }
}
