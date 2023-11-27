package com.qiqiao.service.impl;

import com.qiqiao.model.Result;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.service.UserLoginService;
import org.springframework.stereotype.Service;

/**
 * @author Simon
 * 验证码登录
 */
@Service
public class CheckCodeUserLoginServiceImpl implements UserLoginService {
    @Override
    public Result<String> login(CheckLoginForm checkLoginForm) {
        return null;
    }
}
