package jhshadi.com.taki.services.exceptions;

public class GameDoesNotExistsException extends Exception {

    private static final String NAME_GAME_DONT_EXIST_ERROR = "Name game doesn't exist!";

    @Override
    public String getMessage() {
        return NAME_GAME_DONT_EXIST_ERROR;
    }
}
