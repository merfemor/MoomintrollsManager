package utils;

public class Random {
    public static int randomInt(int from, int to) {
        return ((int)(Math.random() * Integer.MAX_VALUE) % (to - from + 1)) + from;
    }
}
