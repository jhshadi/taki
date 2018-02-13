package jhshadi.com.taki.services.exceptions;

public class DuplicateGameNameException extends Exception {

    private static final String GAME_ALREADY_EXIST_EXCEPTION = "The game name is already exist!";

    @Override
    public String getMessage() {
        return GAME_ALREADY_EXIST_EXCEPTION;
    }
}
