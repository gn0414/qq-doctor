package com.qiqiao.model.user.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author Simon
 * 用户提交登录表单
 */
@Data
@NoArgsConstructor
public class CheckLoginForm implements Serializable {

    private static final long serialVersionUID = -3258839839160856613L;

    private String phone;

    private String checkCode;

    private String password;

    private String wechatId;

    private String type;
}
