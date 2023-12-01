package com.qiqiao.model.user.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Simon
 * token携带数据
 */
@Data
public class UserBaseInfoToken implements Serializable {
    private static final long serialVersionUID = -3258839839160856613L;

    private Long Id;

    private String phone;

    private String nickName;

    private String icon;

    private String wechatNum;
}

