package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jhshadi.com.taki.controllers.controllersUtils.ControllerConsts;
import jhshadi.com.taki.controllers.controllersUtils.ResponseErrorMsg;
import jhshadi.com.taki.services.exceptions.InvalidEventException;
import jhshadi.com.taki.services.exceptions.InvalidPlayerException;
import jhshadi.com.taki.services.vos.Event;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@RestController
public class GetEventsController extends TakiController {

    @RequestMapping("/GetEvents")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");

        HttpSession session = request.getSession();

        try {
            List<Event> events = takiService.getEvents(
                    (int) session.getAttribute(ControllerConsts.PLAYER_ID_SESSION_PARAMETER),
                    (int) session.getAttribute(ControllerConsts.EVENT_ID_SESSION_PARAMETER) + 1);

            updateEventId(events, session);

            ObjectNode eventsJson = JsonNodeFactory.instance.objectNode();
            eventsJson.set("events", mapper.valueToTree(events));

            response.getWriter().print(eventsJson);

        } catch (InvalidEventException | InvalidPlayerException ex) {
            ResponseErrorMsg.createErrorMsg(response, ex.getMessage());
        } catch (NullPointerException ex) {
            ResponseErrorMsg.createErrorMsg(response, ControllerConsts.SESSION_PARAMETERS_DONT_EXIST_ERROR);
        }
    }

    private void updateEventId(List<Event> events, HttpSession session) {
        if (0 < events.size()) {
            int eventId = (events.get(events.size() - 1)).getId();

            session.setAttribute(ControllerConsts.EVENT_ID_SESSION_PARAMETER, eventId);
        }
    }
}
