package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jhshadi.com.taki.controllers.controllersUtils.ControllerConsts;
import jhshadi.com.taki.controllers.controllersUtils.ResponseErrorMsg;
import jhshadi.com.taki.services.exceptions.GameDoesNotExistsException;
import jhshadi.com.taki.services.exceptions.InvalidPlayerParameterException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class JoinGameController extends TakiController {

    // Error Strings
    private static final String NAME_GAME_DONT_EXIST_ERROR = "Name game doesn't exist!";
    private static final String INVALID_PLAYER_NAME_ERROR = "Invalid player name!";
    private static final String EMPTY_PLAYER_NAME_ERROR = "'Player Name' field can't be empty!";
    // Parameters
    private static final String GAME_NAME_PARAMETER = "gameName";
    private static final String PLAYER_NAME_PARAMETER = "playerName";

    @RequestMapping("/JoinGame")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        String gameName = request.getParameter(GAME_NAME_PARAMETER).trim();
        String playerName = request.getParameter(PLAYER_NAME_PARAMETER).trim();
        int playerId;

        try {
            if (playerName.trim().isEmpty()) {
                throw new NullPointerException();
            }

            playerId = takiService.joinGame(gameName, playerName);
            setSession(request, gameName, playerName, playerId);
            response.getWriter().print(JsonNodeFactory.instance.objectNode());

        } catch (NullPointerException ex) {
            ResponseErrorMsg.createErrorMsg(response, EMPTY_PLAYER_NAME_ERROR);
        } catch (GameDoesNotExistsException ex) {
            ResponseErrorMsg.createErrorMsg(response, NAME_GAME_DONT_EXIST_ERROR);
        } catch (InvalidPlayerParameterException ex) {
            ResponseErrorMsg.createErrorMsg(response, INVALID_PLAYER_NAME_ERROR);
        }
    }

    private void setSession(HttpServletRequest request, String gameName, String playerName, int playerId) {
        HttpSession session = request.getSession(true);

        session.setAttribute(ControllerConsts.GAME_NAME_SESSION_PARAMETER, gameName);
        session.setAttribute(ControllerConsts.PLAYER_ID_SESSION_PARAMETER, playerId);
        session.setAttribute(ControllerConsts.PLAYER_NAME_SESSION_PARAMETER, playerName);
        session.setAttribute(ControllerConsts.EVENT_ID_SESSION_PARAMETER, -1);
    }
}
