package service;


import exception.InvalidRomanNumberException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
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
        private static final Map<String, Integer> maxOccurrences = new HashMap<>() {{
            put("I", 3);
            put("X", 4);
            put("C", 4);
            put("M", 4);
            put("V", 1);
            put("L", 1);
            put("D", 1);
        }};

        private static int get(String s) {
            return romToAr.get(s);
        }

        private static String get(Integer s) {
            return arToRom.get(s);
        }

        private static int maxOccurrences(String s) {
            return maxOccurrences.get(s);
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
        String[] input = s.split("");
        Map<String, Long> romans = getAsTreeMapWithValidatedOccurrences(input);
        Iterator<String> iterator = new LinkedHashSet<>(romans.keySet()).iterator();
        Set<String> expectations = new HashSet<>();
        String ordered = iterator.next();
        for (int i = input.length - 1; i >= 0; i--) {
            String real = input[i];
            if (romans.containsKey(ordered) && romans.get(ordered) > 1 || Num.maxOccurrences(ordered) == 1) {
                expectations.add(ordered);
            } else {
                if (expectations.isEmpty()) {
                    expectations.addAll(romans.keySet().stream()
                            .filter(n -> Num.get(n) >= Math.ceil((double) Num.get(real) / 10)).toList());
                }
            }
            if (expectations.contains(real)) {
                romans.computeIfPresent(real, (k, v) -> v - 1);
            } else {
                throw new InvalidRomanNumberException("Number contains illegal order of digits.");
            }
            if (!ordered.equals(real) && romans.get(real) == 0) {
                romans.remove(real);
                expectations.clear();
            }
            if (romans.get(ordered) == 0) {
                romans.remove(ordered);
                if (iterator.hasNext()) {
                    ordered = iterator.next();
                }
                expectations.clear();
            }
        }
        return s;
    }
    private static Map<String, Long> getAsTreeMapWithValidatedOccurrences(String[] input) {
        AtomicInteger count = new AtomicInteger();
        AtomicInteger index = new AtomicInteger();
        Map<String, Long> romans = Arrays.stream(input)
                .collect(Collectors.groupingBy(n -> {
                    if (n.equals(input[index.get()])) {
                        if (count.incrementAndGet() > 3) {
                            throw new InvalidRomanNumberException(String.format(
                                    "Digit '%s' is used more than 3 times in a row.", n));
                        }
                    } else {
                        index.incrementAndGet();
                        count.set(0);
                    }
                    return n;
                }, () -> new TreeMap<>(Comparator.comparingInt(Num::get)), Collectors.counting()));
        romans.forEach((num, occurrences) -> {
            if (Num.maxOccurrences(num) < occurrences)
                throw new InvalidRomanNumberException(String.format(
                        "Digit '%s' is used %d times. Max possible: %d. ", num, occurrences, Num.maxOccurrences(num)));
        });
        return romans;
    }
}



