package service;


import exception.InvalidRomanNumberException;

import java.util.*;
import java.util.stream.Stream;


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

        private static int getRoman(String s) {
            return romToAr.get(s);
        }

        private static String getRoman(Integer s) {
            return arToRom.get(s);
        }

        private static Stream<String> getRomanStream() {
            return romToAr.keySet().stream();
        }
    }

    public static String convert(int n) {
        return arabicToRome(n);
    }

    public static int convert(String s) {
        return romeToArabic(validateRoman(s));
    }

    private static String arabicToRome(int number) {
        StringBuilder result = new StringBuilder();
        String current;
        for (int i = 1, digit = number % 10; number > 0; i *= 10, number /= 10, digit = number % 10) {
            if (digit == 0) {
                continue;
            }
            if (digit < 4) {
                current = Num.getRoman(i).repeat(digit);
            } else if (digit == 4 || digit == 9) {
                current = Num.getRoman(i) + Num.getRoman(i * (1 + digit));
            } else {
                current = Num.getRoman(i * 5) + Num.getRoman(i).repeat(digit - 5);
            }
            result.insert(0, current);
        }
        return result.toString();
    }


    private static int romeToArabic(String str) {
        int[] numbers = Arrays.stream(str.split("")).mapToInt(Num::getRoman).toArray();
        int temp = numbers[0];
        int result = temp;
        for (int i = 1, current; i < numbers.length; i++) {
            current = numbers[i];
            if (temp < current) {
                current -= temp * 2;
                temp = current;
            }
            result += current;
        }
        return result;
    }

    public static String validateRoman(String num) {
        Map<String, Integer> available = new HashMap<>() {{
            put("I", 3);
            put("X", 4);
            put("C", 4);
            put("M", 4);
            put("V", 1);
            put("L", 1);
            put("D", 1);
        }};
        Map<String, Integer> max = new HashMap<>(available);

        String[] bits = num.split("");
        for (String currentRoman : bits) {
            int currentArabic = Num.getRoman(currentRoman);
            int coefficient = getCoefficient(currentArabic);
            int nextArabic = currentArabic * coefficient;
            int prevArabic = getPrevArabic(nextArabic);
            int prevArabicDivOn10 = getPrevArabicDivisibleOn10Else0(prevArabic, coefficient);
            int nextArabicDivOn10 = getNextArabicDivisibleOn10(currentArabic, coefficient);
            String prevRoman = Num.getRoman(prevArabic);
            String prevRomanDiv10 = Num.getRoman(prevArabicDivOn10);
            String nextRomanDiv10 = Num.getRoman(nextArabicDivOn10);

            if (available.containsKey(currentRoman) && coefficient == 2
                    && available.get(prevRoman) > max.get(prevRoman) - 2) {
                available.remove(currentRoman);
                if (!Objects.equals(available.get(prevRoman), max.get(prevRoman))) {
                    available.remove(prevRoman);
                }
                updateAvailable(currentArabic, available);
            } else if (available.get(currentRoman) > 0 && coefficient != 2) {
                available.put(currentRoman, available.get(currentRoman) - 1);
                if ((prevArabicDivOn10 != 0 && !Objects.equals(max.get(prevRomanDiv10), available.get(prevRomanDiv10)))) {
                    available.remove(prevRomanDiv10);
                }
                if (available.containsKey(nextRomanDiv10)) {
                    if (max.get(currentRoman) - available.get(currentRoman) > 1) {
                        available.remove(nextRomanDiv10);
                    } else {
                        available.put(nextRomanDiv10, 1);
                    }
                }
                updateAvailable(nextArabicDivOn10, available);
            } else {
                throw new InvalidRomanNumberException();
            }

        }
        return num;
    }

    private static int getCoefficient(int n) {
        return getFirstDigit(n) != 5 ? 5 : 2;
    }

    private static int getFirstDigit(int n) {
        return (int) (n / Math.pow(10, (int) Math.log10(n)));
    }

    private static int getPrevArabicDivisibleOn10Else0(int prevArabic, int coefficient) {
        return coefficient == 2 ? prevArabic : prevArabic / coefficient;
    }

    private static int getNextArabicDivisibleOn10(int currentArabic, int coefficient) {
        return coefficient == 5 ? currentArabic * 10 : currentArabic * coefficient;
    }

    private static void updateAvailable(int arabic, Map<String, Integer> available) {
        Num.getRomanStream()
                .filter(n -> !available.containsKey(n) || arabic < Num.getRoman(n) || available.get(n) == 0)
                .forEach(available::remove);

    }

    private static int getPrevArabic(int nextArabic) {
        int temp = nextArabic / 10;
        return temp == 0 ? 1 : temp;

    }
}


