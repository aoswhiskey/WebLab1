package utility;

import exceptions.RequestException;
import exceptions.ValidationException;
import com.fastcgi.FCGIInterface;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
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

    private static final String HTTP_ERROR = """
    Content-Type: application/json
    Content-Length: %d

    %s
    """;

    private static final String ERROR_JSON = """
    {
        "reason": "%s"
    }
    """;

    public String readRequest() throws IOException {
        FCGIInterface.request.inStream.fill();
        var contentLength = FCGIInterface.request.inStream.available();
        var buffer = ByteBuffer.allocate(contentLength);
        var readBytes = FCGIInterface.request.inStream.read(buffer.array(), 0, contentLength);
        var requestBodyRaw = new byte[readBytes];
        buffer.get(requestBodyRaw);
        buffer.clear();

        return new String(requestBodyRaw, StandardCharsets.UTF_8);
    }

    public HashMap<String, Number> checkRequest() throws RequestException {
        String requestMethod = FCGIInterface.request.params.getProperty("REQUEST_METHOD");
        String requestURI = FCGIInterface.request.params.getProperty("REQUEST_URI");

        if ( !( requestMethod.equals(HTTPMethod) ) ) {
            throw new RequestException("Неверный метод запроса");
        }

        if ( !( requestURI.equals(serverHost) ) ) {
            throw new RequestException("Неверный URI запроса");
        }

        try {
            return Validator.validate(readRequest());
        } catch(ValidationException | IOException e) {
            throw new RequestException(e.getMessage());
        }
    }

    public void sendSuccessResponse(long time, boolean result) {
        var json = String.format(RESPONSE_JSON, time, result);
        var response = String.format(HTTP_Response, json.getBytes(StandardCharsets.UTF_8).length + 2, json);
        System.out.println(response);
    }

    public void sendFailResponse(String message) {
        var json = String.format(ERROR_JSON, message);
        var response = String.format(HTTP_ERROR, json.getBytes(StandardCharsets.UTF_8).length + 2, json);
        System.out.println(response);
    }
}