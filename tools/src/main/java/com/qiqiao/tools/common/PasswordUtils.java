package com.qiqiao.tools.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author Simon
 */
public class PasswordUtils {
    /**
     * 盐值长度
     */
    private static final int SALT_LENGTH = 16;
    /**
     *哈希算法
     */

    private static final String HASH_ALGORITHM = "SHA-256";

    /**
     *  生成盐值
     * */
    public static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    /**
     *根据密码和盐值生成加密后的密码
     */
    public static String encryptPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.reset();
        digest.update(salt);
        byte[] hashedBytes = digest.digest(password.getBytes());
        return bytesToHex(hashedBytes);
    }

    /**
     * 字节数组转十六进制字符串
     * */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}