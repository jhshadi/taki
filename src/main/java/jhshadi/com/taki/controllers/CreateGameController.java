package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jhshadi.com.taki.controllers.controllersUtils.ResponseErrorMsg;
import jhshadi.com.taki.services.exceptions.DuplicateGameNameException;
import jhshadi.com.taki.services.exceptions.InvalidPlayerParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class CreateGameController extends TakiController {

    // Error Strings
    private static final String NAME_GAME_ALREDY_EXIST_ERROR = "Name game is already exist!";
    private static final String EMPTY_GAME_NAME_ERROR = "'Game Name' field can't be empty!";
    private static final String INVALID_NUMBER_OF_PLAYERS_ERROR = "Invalid number of players!";
    // Parameters
    private static final String GAME_NAME_PARAMETER = "gameName";
    private static final String HUMAN_PLAYERS_PARAMETER = "humanPlayers";
    private static final String COMPUTER_PLAYERS_PARAMETER = "computerPlayers";

    @RequestMapping("/CreateGame")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        try {
            String gameName = request.getParameter(GAME_NAME_PARAMETER).trim();
            if (gameName == null || gameName.isEmpty()) {
                throw new NullPointerException();
            }
            int humanPlayers = Integer.parseInt(request.getParameter(HUMAN_PLAYERS_PARAMETER));
            int computerPlayers = Integer.parseInt(request.getParameter(COMPUTER_PLAYERS_PARAMETER));
            takiService.createGame(gameName, humanPlayers, computerPlayers);

            response.getWriter().print(JsonNodeFactory.instance.objectNode());

        } catch (NullPointerException ex) {
            ResponseErrorMsg.createErrorMsg(response, EMPTY_GAME_NAME_ERROR);
        } catch (DuplicateGameNameException ex) {
            ResponseErrorMsg.createErrorMsg(response, NAME_GAME_ALREDY_EXIST_ERROR);
        } catch (InvalidPlayerParameterException | NumberFormatException ex) {
            ResponseErrorMsg.createErrorMsg(response, INVALID_NUMBER_OF_PLAYERS_ERROR);
        }
    }
}
