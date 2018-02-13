package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jhshadi.com.taki.controllers.controllersUtils.ResponseErrorMsg;
import jhshadi.com.taki.services.exceptions.GameDoesNotExistsException;
import jhshadi.com.taki.services.vos.GameDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class GetGameDetailsController extends TakiController {

    private static final String GAME_NAME_PARAMETER = "gameName";

    @RequestMapping("/GetGameDetails")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        String gameName = request.getParameter(GAME_NAME_PARAMETER);

        try {
            GameDetails gameDetails = takiService.getGameDetails(gameName);

            ObjectNode gameDetailsJson = JsonNodeFactory.instance.objectNode();
            gameDetailsJson.put("name", gameDetails.getName());
            gameDetailsJson.put("humanPlayers", gameDetails.getHumanPlayers());
            gameDetailsJson.put("joinedHumanPlayers", gameDetails.getJoinedHumanPlayers());
            gameDetailsJson.put("computerizedPlayers", gameDetails.getComputerizedPlayers());

            response.getWriter().print(gameDetailsJson);
        } catch (GameDoesNotExistsException ex) {
            ResponseErrorMsg.createErrorMsg(response, ex.getMessage());
        }
    }
}
