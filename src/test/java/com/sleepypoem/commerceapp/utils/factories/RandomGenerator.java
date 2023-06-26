package com.sleepypoem.commerceapp.utils.factories;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;

import java.time.LocalDateTime;

public class RandomGenerator {

    public static String randomString(int size) {
        return RandomStringUtils.randomAlphabetic(size);
    }

    public static int randomInt(int start, int end) {
        return RandomUtils.nextInt(start, end);
    }

    public static Long randomLong(long start, long end) {
        return RandomUtils.nextLong(start, end);
    }

    public static Double randomDouble(Double start, Double end) {
        return RandomUtils.nextDouble(start, end);
    }

    public static LocalDateTime randomPastDate() {
        return LocalDateTime.of(randomInt(2010, 2022), 7, 25, randomInt(1, 12), 55);
    }


}
