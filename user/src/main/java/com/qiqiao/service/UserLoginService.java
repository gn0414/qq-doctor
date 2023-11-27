package com.qiqiao.service;

import com.qiqiao.model.Result;
import com.qiqiao.model.user.vo.CheckLoginForm;

/**
 * @author Simon
 */
public interface UserLoginService {
    /**
     * fetch 用户登录顶层抽象
     * @param checkLoginForm 用户登录表单
     * @return Result
     */
    Result<String> login(CheckLoginForm checkLoginForm);


}
