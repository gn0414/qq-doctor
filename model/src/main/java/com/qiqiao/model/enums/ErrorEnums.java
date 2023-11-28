package com.qiqiao.model.enums;

/**
 * @author Simon
 * 枚举的key规范:
 * 服务+业务+错误描述(状态由201起始)
 */
public enum ErrorEnums {
    /**
     * 登录方式类型不存在,也就是表单的Type有错误
     */
    USER_LOGIN_WAY_NOT_FUND(201,"Login Type Error:Please Check Your Type"),

    USER_LOGIN_CHECK_CODE_PHONE_NOT_VALID(202,"Phone Not Valid:Please Check Your Phone Number"),

    USER_LOGIN_CHECK_CODE_IP_MAX(203,"This IP Get Too MUCH Message:Please Try After 24H"),

    USER_LOGIN_CHECK_CODE_EXPIRE(204,"Your Code Is Not Expire:Please Wait 5 Minutes"),

    INTERNET_Error(500,"Internet Error:Please Try later");
    private final int statusCode;
    private final String message;

    ErrorEnums(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
