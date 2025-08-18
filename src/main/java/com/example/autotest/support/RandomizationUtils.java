package com.example.autotest.support;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

//TODO: доработать методы этого класса на этапе разработки генерации данных
public final class RandomizationUtils {

    private static final Random RANDOM = new Random();

    private RandomizationUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    public static long getNumber(int length) {
        double min = Math.pow(10, length - 1d);
        double max = Math.pow(10, length) - 1;
        return getNumber(Math.round(min), Math.round(max));
    }

    public static long getNumber(long min, long max) {
        return RANDOM.nextLong(max - min) + min;
    }

    public static int getNumber(int min, int max) {
        return RANDOM.nextInt(max - min) + min;
    }

    public static UUID getUuid() {
        return UUID.randomUUID();
    }

    public static <K, V> List<Map.Entry<K, V>> shuffleMap(Map<K, V> map) {
        return map.entrySet()
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list, RANDOM);
                    return list;
                }));
    }

    private static String generateString(char minCharCode, char maxCharCode, int length) {
        StringBuilder wordBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            wordBuilder.append((char) getNumber(minCharCode, maxCharCode));
        }

        return wordBuilder.toString();
    }
}
