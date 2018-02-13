package jhshadi.com.taki.controllers.controllersUtils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseErrorMsg {

    private static final String JSON_ERROR_PARAMETER = "error";

    public static void createErrorMsg(HttpServletResponse response, String msg) throws IOException {
        ObjectNode jsonError = JsonNodeFactory.instance.objectNode();

        jsonError.put(JSON_ERROR_PARAMETER, msg);

        response.getWriter().print(jsonError);
    }
}
