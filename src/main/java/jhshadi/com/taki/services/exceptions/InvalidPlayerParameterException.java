package jhshadi.com.taki.services.exceptions;

public class InvalidPlayerParameterException extends Exception {

    private static final String INVALID_PLAYER_PARAMETERS_EXCEPTION = "Invalid players parameters!";

    @Override
    public String getMessage() {
        return INVALID_PLAYER_PARAMETERS_EXCEPTION;
    }
}
