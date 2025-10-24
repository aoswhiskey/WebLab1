package utility;

import exceptions.RequestException;
import exceptions.ValidationException;
import com.fastcgi.FCGIInterface;
import java.util.HashMap;

public class HTTPHandler {
    private static final String serverHost = "/calculate";
    private static final String HTTPMethod = "POST";

    private static final String HTTP_Response = """
        Content-Type: application/json
        Content-Length: %d
        
        %s
        """;

    private static final String RESPONSE_JSON = """
    {
        "time": "%s",
        "result": %b
    }
        """;

    private static final String ERROR_JSON = """
    {
        "reason": "%s"
    }
    """;

    public HashMap<String, Number> checkRequest() throws RequestException {
        String requestMethod = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
        String requestURI = FCGIInterface.request.params.getProperty("REQUEST_URI");

        if (!HTTPMethod.equals(requestMethod)) {
            throw new RequestException("Неверный метод запроса. Разрешен только POST");
        }

        if (!requestURI.startsWith(serverHost)) {
            throw new RequestException("Неверный URI запроса");
        }

        try {
            String queryString = FCGIInterface.request.params.getProperty("QUERY_STRING");
            return Validator.validateGetRequest(queryString);
        } catch(ValidationException e) {
            throw new RequestException(e.getMessage());
        }
    }

    public void sendSuccessResponse(long time, boolean result) {
        var json = String.format(RESPONSE_JSON, time, result);
        var response = String.format(HTTP_Response, json.getBytes(java.nio.charset.StandardCharsets.UTF_8).length + 2, json);
        System.out.println(response);
    }

    public void sendFailResponse(String message) {
        var json = String.format(ERROR_JSON, message);
        var response = String.format(HTTP_Response, json.getBytes(java.nio.charset.StandardCharsets.UTF_8).length + 2, json);
        System.out.println(response);
    }
}