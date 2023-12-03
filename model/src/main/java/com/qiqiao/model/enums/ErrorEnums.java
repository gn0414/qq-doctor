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
    USER_LOGIN_WAY_NOT_FUND(201,"登陆方式错误,请选择已有的登陆方式"),

    USER_LOGIN_CHECK_CODE_PHONE_NOT_VALID(202,"您输入的手机号不合法,请检查您的手机号"),

    USER_LOGIN_CHECK_CODE_IP_MAX(203,"您的IP获取了太多短信,请在24小时后重试"),

    USER_LOGIN_CHECK_CODE_EXPIRE(204,"您的验证码未过期,请在5分钟后重试"),

    USER_LOGIN_CHECK_CODE_NOT_VALID(205,"您输入的验证码不合法,请检查您的验证码"),

    USER_LOGIN_CHECK_CODE_FAIL(206,"您输入的验证码不正确,登陆失败"),

    USER_LOGIN_ONLINE(207,"对不起您已在线,只能登录一次"),
    USER_LOGIN_BAN(208,"对不起您的账户已被封禁,请联系管理员"),

    USER_LOGIN_PASSWORD_ERROR(209,"对不起您的密码有误"),
    INTERNET_Error(500,"网络异常,请稍后重试~");
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
