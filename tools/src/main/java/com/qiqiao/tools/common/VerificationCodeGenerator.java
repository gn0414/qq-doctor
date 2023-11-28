package com.qiqiao.tools.common;

import java.util.Random;


/**
 * @author Simon
 * 验证码生成工具类
 */
public class VerificationCodeGenerator {
    private static final String CHARACTERS = "0123456789";
    private static final int CODE_LENGTH = 6;

    public static String generateVerificationCode() {
        Random random = new Random();
        StringBuilder codeBuilder = new StringBuilder(CODE_LENGTH);

        // 生成第一位字符
        // 减去1是为了排除数字0
        int firstIndex = random.nextInt(CHARACTERS.length() - 1) + 1;
        char firstChar = CHARACTERS.charAt(firstIndex);
        codeBuilder.append(firstChar);

        // 生成剩余字符
        for (int i = 0; i < CODE_LENGTH - 1; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            codeBuilder.append(randomChar);
        }

        return codeBuilder.toString();
    }
}