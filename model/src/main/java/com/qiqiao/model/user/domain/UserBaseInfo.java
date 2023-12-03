package com.qiqiao.model.user.domain;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * @author Simon
 * 用户基础信息类（id、密码、昵称、头像、其他登录信息(暂时vx)、创建、修改时间）
 * 注意我们的数据库表的id一定要是我们的bigint类型
 */
@TableName("user_base_info")
@Data
public class UserBaseInfo {
    @TableId
    private Long id;

    @TableField("phone")
    private String phone;

    @TableField("password")
    private String password;

    @TableField("salt")
    private String salt;

    @TableField("nick_name")
    private String nickName;

    @TableField("icon")
    private String icon;

    @TableField("wechat_num")
    private String wechatNum;

    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE,select = false)
    private LocalDateTime updateTime;

    @TableField(value = "is_ban")
    private String isBan;

    @TableLogic
    private String isDelete;


}
