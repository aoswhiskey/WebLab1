package utility;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import exceptions.ValidationException;

public class Validator {

    public static HashMap<String, Number> validateGetRequest(String queryString) throws ValidationException {
        if (queryString == null || queryString.isEmpty()) {
            throw new ValidationException("Отсутствуют параметры запроса");
        }
        
        HashMap<String, Number> params = parseQueryString(queryString);
        
        if (params.size() != 3) {
            throw new ValidationException("Incorrect argument's count");
        }
        
        if (!(params.containsKey("x") && params.containsKey("y") && params.containsKey("r"))) {
            throw new ValidationException("Incorrect arguments");
        }
        
        params.put("y", new BigDecimal(String.valueOf(params.get("y")))
            .setScale(10, RoundingMode.DOWN).doubleValue());

        if (!isNumberInRange(params.get("x"), -2, 2)) {
            throw new ValidationException("'x' argument must be a multiple of 0.5 in range [-2, 2]");
        }

        if (!isNumberInRange(params.get("r"), 1, 3)) {
            throw new ValidationException("'r' argument must be a multiple of 0.5 in range [1, 3]");
        }

        if ((params.get("y").doubleValue() <= -5) || (params.get("y").doubleValue() >= 5)) {
            throw new ValidationException("'y' argument must be a double in range (-5, 5)");
        }

        return params;
    }
    
    private static HashMap<String, Number> parseQueryString(String queryString) throws ValidationException {
        HashMap<String, Number> params = new HashMap<>();
        
        try {
            String[] pairs = queryString.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    String key = keyValue[0];
                    String value = keyValue[1];
                    
                    double numericValue = Double.parseDouble(value);
                    params.put(key, numericValue);
                }
            }
            
            return params;
            
        } catch (NumberFormatException e) {
            throw new ValidationException("Неверный формат числового параметра");
        } catch (Exception e) {
            throw new ValidationException("Ошибка парсинга параметров: " + e.getMessage());
        }
    }

    public static boolean isNumberInRange(Number number, int min, int max) {
        double value = number.doubleValue();
        
        if (Math.abs(value % 0.5) > 1e-10) {
            return false;
        }
        
        return value >= min && value <= max;
    }
}