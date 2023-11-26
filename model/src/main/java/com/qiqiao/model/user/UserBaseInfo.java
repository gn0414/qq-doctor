package com.qiqiao.model.user;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

/**
 * @author Simon
 * 用户基础信息类（id、密码、昵称、头像、其他登录信息(暂时vx)、创建、修改时间）
 * 注意我们的数据库表的id一定要是我们的bigint类型
 */
@TableName("user_base_info")
public class UserBaseInfo {
    @TableId
    private Long id;

    @TableField("phone")
    private String phone;

    @TableField("password")
    private String password;

    @TableField("nick_name")
    private String niceName;

    @TableField("icon")
    private String icon;

    @TableField("wechat_num")
    private String wechatNum;

    @TableField("creat_time")
    private Date createTime;

    @TableField("update_time")
    private Date updateTime;


}
