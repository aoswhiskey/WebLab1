import exceptions.RequestException;
import com.fastcgi.FCGIInterface;
import utility.Checker;
import utility.HTTPHandler;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    static {
        try {
            FileHandler fileHandler = new FileHandler("server.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch(IOException e) {
            logger.info("ERROR: " + e);
        }
    }

    public static void main(String[] args) {
        var fcgiInterface = new FCGIInterface();
        HTTPHandler httpHandler = new HTTPHandler();
        while (fcgiInterface.FCGIaccept() >= 0) {
            var scriptStartTime = Instant.now();
            try {
                HashMap<String, Number> params = httpHandler.checkRequest();
                boolean result = Checker.checkParams(params.get("x").intValue(), params.get("y").doubleValue(), params.get("r").intValue());
                var scriptEndTime = Instant.now();
                httpHandler.sendSuccessResponse(ChronoUnit.MICROS.between(scriptStartTime, scriptEndTime), result);
            } catch(RequestException e) {
                httpHandler.sendFailResponse(e.getMessage());
            }
        }
    }
}
