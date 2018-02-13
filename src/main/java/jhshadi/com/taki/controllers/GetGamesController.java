package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class GetGamesController extends TakiController {

    @RequestMapping("/GetGames")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        List<String> waitingGames = takiService.getWaitingGames();
        List<String> activeGames = takiService.getActiveGames();

        ObjectNode jsonObj = JsonNodeFactory.instance.objectNode();
        jsonObj.set("waitingGames", mapper.valueToTree(waitingGames));
        jsonObj.set("activeGames", mapper.valueToTree(activeGames));

        response.getWriter().print(jsonObj);
    }
}
