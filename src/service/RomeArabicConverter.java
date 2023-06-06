package service;


import exception.InvalidRomanNumberException;

import java.util.*;
import java.util.stream.Collectors;


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
                current = Num.get(i).repeat(digit);
            } else if (digit == 4 || digit == 9) {
                current = Num.get(i) + Num.get(i * (1 + digit));
            } else {
                current = Num.get(i * 5) + Num.get(i).repeat(digit - 5);
            }
            result.insert(0, current);
        }
        return result.toString();
    }


    private static int romeToArabic(String str) {
        int[] numbers = Arrays.stream(str.split("")).mapToInt(Num::get).toArray();
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

    public static String validateRoman(String s) {
        Map<String, Integer> max = new HashMap<>() {{
            put("I", 3);
            put("X", 4);
            put("C", 4);
            put("M", 4);
            put("V", 1);
            put("L", 1);
            put("D", 1);
        }};
        String[] romans = s.split("");
        Map<String, Long> col = Arrays.stream(romans)
                .collect(Collectors.groupingBy(n -> n, () -> new TreeMap<>(Comparator.comparingInt(Num::get)), Collectors.counting()));
        col.forEach((key, value) -> {
            if (max.get(key) < value) throw new InvalidRomanNumberException();
        });
        Iterator<String> iterator = new LinkedHashSet<>(col.keySet()).iterator();
        Set<String> expectations = new HashSet<>();
        String ordered = iterator.next();
        for (int i = romans.length - 1; i >= 0; i--) {
            String real = romans[i];
            if (col.containsKey(ordered) && col.get(ordered) > 1 || getFirstDigit(Num.get(ordered)) == 5) {
                expectations.add(ordered);
            } else {
                String t = ordered;
                if (expectations.isEmpty()) {
                    expectations.addAll(col.keySet().stream().filter(n -> Num.get(n) >= Num.get(t)).toList());
                }
            }
            if (expectations.contains(real)) {
                col.computeIfPresent(real, (k, v) -> v - 1);
            } else {
                throw new InvalidRomanNumberException();
            }
            if (!ordered.equals(real) && col.get(real) == 0) {
                col.remove(real);
                expectations.clear();
            }
            if (col.get(ordered) == 0) {
                col.remove(ordered);
                if (iterator.hasNext()) {
                    ordered = iterator.next();
                }
                expectations.clear();
            }
        }
        return s;
    }

    private static int getFirstDigit(int n) {
        return (int) (n / Math.pow(10, (int) Math.log10(n)));
    }
}


