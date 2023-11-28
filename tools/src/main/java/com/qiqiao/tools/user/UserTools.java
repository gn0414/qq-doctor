package com.qiqiao.tools.user;

import java.util.regex.Pattern;

/**
 * @author Simon
 * 用户服务工具箱
 */
public class UserTools {
    private static final String PHONE_REGEX = "^1\\d{10}$";
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);

    /**
     * 检验合法手机号
     * @param phone 手机号
     * @return boolean
     */
    public static boolean isValidPhone(String phone) {
        return PHONE_PATTERN.matcher(phone).matches();
    }

}
