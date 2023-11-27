package com.qiqiao.model.user.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author Simon
 * 用户提交登录表单
 */
@Data
@Builder
public class CheckLoginForm {

    private String phone;

    private String checkCode;

    private String password;

    private String wechatId;
}
