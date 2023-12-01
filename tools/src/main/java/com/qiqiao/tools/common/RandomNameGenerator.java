package com.qiqiao.tools.common;

import java.util.Random;

/**
 * @author Simon
 * 随机姓名
 */
public class RandomNameGenerator {
    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NAME_LENGTH = 6;
    private static final Random RANDOM = new Random();

    public static String generateRandomName() {
        StringBuilder stringBuilder = new StringBuilder(NAME_LENGTH);
        for (int i = 0; i < NAME_LENGTH; i++) {
            int randomIndex = RANDOM.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}