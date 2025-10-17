package utility;

public class Checker {
    public static boolean checkParams(double x, double y, double r) {
        boolean isInFirstQuarter = (y >= 0) && (x >= 0);
        boolean isInSecondQuarter = (y >= 0) && (x <= 0);
        boolean isInThirdQuarter = (y <= 0) && (x <= 0);
        boolean isInFourthQuarter = (y <= 0) && (x >= 0);

        return ( (isInFirstQuarter & (y <= (-0.5 * x + 0.5 * r))) || (isInThirdQuarter & (y >= - 0.5 * r) & (x >= -r)) || (isInFourthQuarter & (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2))) ) & !isInSecondQuarter;
    }
}