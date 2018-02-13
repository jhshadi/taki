package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jhshadi.com.taki.controllers.controllersUtils.ControllerConsts;
import jhshadi.com.taki.controllers.controllersUtils.ResponseErrorMsg;
import jhshadi.com.taki.services.exceptions.InvalidCardException;
import jhshadi.com.taki.services.exceptions.InvalidPlayerException;
import jhshadi.com.taki.services.vos.Card;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
public class ThrowCardsController extends TakiController {

    private static final String CARDS_PARAMETER = "cards";

    @RequestMapping("/ThrowCards")
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession();

        if (session != null) {
            String cards = request.getParameter(CARDS_PARAMETER);
            List<Card> cardsList = mapper.readValue(cards, mapper.getTypeFactory().constructCollectionType(List.class, Card.class));

            try {
                takiService.throwCards(
                        (int) session.getAttribute(ControllerConsts.PLAYER_ID_SESSION_PARAMETER),
                        (int) session.getAttribute(ControllerConsts.EVENT_ID_SESSION_PARAMETER),
                        cardsList);

                response.getWriter().print(JsonNodeFactory.instance.objectNode());
            } catch (InvalidPlayerException | InvalidCardException ex) {
                ResponseErrorMsg.createErrorMsg(response, ex.getMessage());
            } catch (NullPointerException ex) {
                ResponseErrorMsg.createErrorMsg(response, ControllerConsts.SESSION_PARAMETERS_DONT_EXIST_ERROR);
            }
            
        } else {
            ResponseErrorMsg.createErrorMsg(response, ControllerConsts.SESSION_DONT_EXIST_ERROR);
        }
    }
}
