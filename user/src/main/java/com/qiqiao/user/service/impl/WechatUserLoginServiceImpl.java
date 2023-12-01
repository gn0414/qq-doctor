package com.qiqiao.user.service.impl;

import com.qiqiao.model.Result;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.user.service.UserLoginService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Simon
 * 微信登录实现类
 */
@Service
public class WechatUserLoginServiceImpl implements UserLoginService {
    @Override
    public Result<Void> login(CheckLoginForm checkLoginForm, HttpServletResponse response) {
        return Result.successNoData();
    }
}
