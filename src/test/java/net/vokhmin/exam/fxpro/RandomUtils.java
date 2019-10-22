package net.vokhmin.exam.fxpro;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class RandomUtils {

    private static final String ALPHABETIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "abcdefghijklmnopqrstuvxyz";
    private static final String ALPHA_NUMERIC_STRING = ALPHABETIC_STRING + "0123456789";

    private static ThreadLocal<Random> random = new ThreadLocal<Random>() {
        protected Random initialValue() {
            return new Random();
        }
    };

    public RandomUtils() {
    }

    public static String randomAlphabetic(int count) {
        return randomString(ALPHABETIC_STRING, count);
    }

    static String randomAlphanumeric(int count) {
        return randomString(ALPHA_NUMERIC_STRING, count);
    }

    private static String randomString(String charSet, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(charSet.charAt(nextInt(charSet.length())));
        }
        return sb.toString();
    }

    public static int nextInt(int max) {
        return random.get().nextInt(max);
    }

    public static long nextLong() {
        return Math.abs(random.get().nextLong() + 1);
    }

    public static double nextDouble() {
        return random.get().nextDouble();
    }

    public static double nextDouble(int scale) {
        return BigDecimal.valueOf(nextDouble()).setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

    public static BigDecimal nextBigDecimal() {
        return BigDecimal.valueOf(random.get().nextDouble());
    }

    public static <T> T randomArrayValue(T... values) {
        int i = nextInt(values.length);
        return values[i];
    }

    public static <T extends Enum<T>> T randomEnumValue(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return randomArrayValue(enumConstants);
    }

    public static <T extends Enum<T>> T randomEnumValue(Class<T> enumClass, T... exceptValues) {
        Set<T> allValuesSet = new HashSet<>(Arrays.asList(enumClass.getEnumConstants()));
        Set<T> exceptValuesSet = new HashSet<>(Arrays.asList(exceptValues));
        allValuesSet.removeAll(exceptValuesSet);
        return (T) randomArrayValue(allValuesSet.toArray());
    }

}
