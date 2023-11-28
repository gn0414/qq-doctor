package com.qiqiao.model.messagqueue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * @author Simon
 * 短信参数消息模板
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageVo implements Serializable {

    private static final long serialVersionUID = -3258839839160856613L;
    private String phone;

    private String code;

    private String id;

    private String secret;
}
