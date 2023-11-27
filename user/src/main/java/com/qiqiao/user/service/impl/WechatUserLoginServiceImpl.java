package com.qiqiao.user.service.impl;

import com.qiqiao.model.Result;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.user.service.UserLoginService;
import org.springframework.stereotype.Service;

/**
 * @author Simon
 * 微信登录实现类
 */
@Service
public class WechatUserLoginServiceImpl implements UserLoginService {
    @Override
    public Result<String> login(CheckLoginForm checkLoginForm) {
        return Result.success("微信登录");
    }
}
