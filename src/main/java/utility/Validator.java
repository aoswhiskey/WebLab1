package utility;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import exceptions.ValidationException;

public class Validator {

    private static final Gson gson = new Gson();
    public static HashMap<String, Number> validate(String JSONRequest) throws ValidationException {
        try {
            HashMap<String, Number> map = gson.fromJson(JSONRequest, new TypeToken<HashMap<String, Number>>() {
            }.getType());
            map.put("y", new BigDecimal(String.valueOf(map.get("y"))).setScale(10, RoundingMode.DOWN).doubleValue());

            if (map.size() != 3) {
                throw new ValidationException("Incorrect argument's count");
            }
            if (!(map.containsKey("x") && map.containsKey("y") && map.containsKey("r"))) {
                throw new ValidationException("Incorrect arguments");
            }
            if (!isIntegerInRange(map.get("x"), -3, 5)) {
                throw new ValidationException("'x' argument must be an integer in range [-5, 3]");
            }

            if (!isIntegerInRange(map.get("r"), 1, 5)) {
                throw new ValidationException("'r' argument must be an integer in range [1, 5]");
            }

            if ((map.get("y").doubleValue() < -3) || (map.get("y").doubleValue() > 5)) {
                throw new ValidationException("'y' argument must be a double in range (-3, 3)");
            }

            return map;
        } catch (ValidationException e){
            throw e;
        } catch (Exception e){
            throw new ValidationException("Incorrect argument's values");
        }
    }

    public static boolean isIntegerInRange(Number number, int min, int max) {
        // Checking if a number is an integer
        if (number.doubleValue() % 1 != 0) {
            return false; // Не целое число
        }

        // Cast the number to int and check the range
        int intNumber = number.intValue();
        return intNumber >= min && intNumber <= max;
    }
}