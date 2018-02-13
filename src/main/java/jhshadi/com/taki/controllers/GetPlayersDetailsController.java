package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jhshadi.com.taki.controllers.controllersUtils.ControllerConsts;
import jhshadi.com.taki.controllers.controllersUtils.ResponseErrorMsg;
import jhshadi.com.taki.services.exceptions.GameDoesNotExistsException;
import jhshadi.com.taki.services.vos.PlayerDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
public class GetPlayersDetailsController extends TakiController {

    // Error Strings
    private static final String NAME_GAME_DONT_EXIST_ERROR = "Name game doesn't exist!";

    @RequestMapping("/GetPlayersDetails")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession();

        if (session != null) {

            try {
                List<PlayerDetails> players = takiService.getPlayersDetails(
                        (String) session.getAttribute(ControllerConsts.GAME_NAME_SESSION_PARAMETER),
                        (int) session.getAttribute(ControllerConsts.PLAYER_ID_SESSION_PARAMETER));

                JsonNode playersDetails = convertPlayersDetailsListToJSON((String) session.getAttribute(ControllerConsts.PLAYER_NAME_SESSION_PARAMETER), players);

                response.getWriter().print(playersDetails);

            } catch (GameDoesNotExistsException ex) {
                ResponseErrorMsg.createErrorMsg(response, NAME_GAME_DONT_EXIST_ERROR);
            } catch (NullPointerException ex) {
                ResponseErrorMsg.createErrorMsg(response, ControllerConsts.SESSION_PARAMETERS_DONT_EXIST_ERROR);
            }

        } else {
            ResponseErrorMsg.createErrorMsg(response, ControllerConsts.SESSION_DONT_EXIST_ERROR);
        }
    }

    private JsonNode convertPlayersDetailsListToJSON(String playerName, List<PlayerDetails> players) {
        ObjectNode result = JsonNodeFactory.instance.objectNode();

        ArrayNode playersDetails = JsonNodeFactory.instance.arrayNode();
        JsonNode currentPlayerDetails = JsonNodeFactory.instance.objectNode();

        for (PlayerDetails player : players) {
            ObjectNode playerDetailJson = mapper.valueToTree(player);

            if (player.getName().equals(playerName)) {
                currentPlayerDetails = playerDetailJson;
            } else {
                playerDetailJson.remove("cards");
                playersDetails.add(playerDetailJson);
            }
        }

        result.set("playersDetails", playersDetails);
        result.set("currentPlayerDetails", currentPlayerDetails);

        return result;
    }
}
