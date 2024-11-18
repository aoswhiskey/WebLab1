package utility;

public class Checker {
    public static boolean checkParams(int x, double y, int r) {
        boolean isInFirstQuarter = (y >= 0) && (x >= 0);
        boolean isInSecondQuarter = (y >= 0) && (x <= 0);
        boolean isInThirdQuarter = (y <= 0) && (x <= 0);
        boolean isInFourthQuarter = (y <= 0) && (x >= 0);

        return ( (isInFirstQuarter & (y <= (-x + 0.5 * r))) || (isInSecondQuarter & (y <= r) & (x >= -r*0.5)) || (isInThirdQuarter & (Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2))) ) & !isInFourthQuarter;
    }
}