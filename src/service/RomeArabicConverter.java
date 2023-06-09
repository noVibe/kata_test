package service;


import exception.InvalidRomanNumberException;
import exception.UnexpectedResultException;

import java.util.*;

public class RomeArabicConverter {
    private final static class Num {
        private static final Map<Integer, String> arToRom = new HashMap<>() {{
            put(1, "I");
            put(10, "X");
            put(100, "C");
            put(1000, "M");
            put(5, "V");
            put(50, "L");
            put(500, "D");
        }};
        private static final Map<String, Integer> romToAr = new HashMap<>() {{
            put("I", 1);
            put("X", 10);
            put("C", 100);
            put("M", 1000);
            put("V", 5);
            put("L", 50);
            put("D", 500);
        }};

        private static int get(String s) {
            return romToAr.get(s);
        }

        private static String get(Integer s) {
            return arToRom.get(s);
        }
    }

    public static String convert(int n) {
        return arabicToRome(n);
    }

    public static int convert(String s) {
        return romeToArabic(s);
    }

    private static String arabicToRome(int number) {
        StringBuilder result = new StringBuilder();
        try {
            for (int i = 1, digit = number % 10; number > 0; i *= 10, number /= 10, digit = number % 10) {
                String current;
                if (digit < 4) {
                    current = Num.get(i).repeat(digit);
                } else if (digit == 4 || digit == 9) {
                    current = Num.get(i) + Num.get(i * (1 + digit));
                } else {
                    current = Num.get(i * 5) + Num.get(i).repeat(digit - 5);
                }
                result.insert(0, current);
            }
            if (result.indexOf("null") != -1) {
                throw new NullPointerException();
            }
            return result.toString();
        } catch (NullPointerException e) {
            throw new UnexpectedResultException("Result is too big for Roman format.");
        }
    }


    private static int romeToArabic(String str) {
        return Arrays.stream(str.split("")).map(Num::get).reduce((x, y) -> x >= y ? x + y : y - x)
                .filter(x -> str.equals(arabicToRome(x)))
                .orElseThrow(() -> new InvalidRomanNumberException("Impossible Roman number: " + str));
    }

}



