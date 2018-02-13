package jhshadi.com.taki.controllers;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import jhshadi.com.taki.controllers.controllersUtils.ControllerConsts;
import jhshadi.com.taki.controllers.controllersUtils.ResponseErrorMsg;
import jhshadi.com.taki.services.exceptions.InvalidPlayerException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
public class ResignController extends TakiController {

    @RequestMapping("/Resign")
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession();

        if (session != null) {
            try {
                takiService.resign((int) session.getAttribute(ControllerConsts.PLAYER_ID_SESSION_PARAMETER));

                response.getWriter().print(JsonNodeFactory.instance.objectNode());
            } catch (InvalidPlayerException ex) {
                ResponseErrorMsg.createErrorMsg(response, ex.getMessage());
            } catch (NullPointerException ex) {
                ResponseErrorMsg.createErrorMsg(response, ControllerConsts.SESSION_PARAMETERS_DONT_EXIST_ERROR);
            }
            
        } else {
            ResponseErrorMsg.createErrorMsg(response, ControllerConsts.SESSION_DONT_EXIST_ERROR);
        }
    }
}
