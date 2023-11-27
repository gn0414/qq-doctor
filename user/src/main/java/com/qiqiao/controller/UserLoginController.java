package com.qiqiao.controller;

import com.qiqiao.model.Result;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.service.UserLoginService;
import com.qiqiao.service.impl.CheckCodeUserLoginServiceImpl;
import com.qiqiao.service.impl.PasswordUserLoginServiceImpl;
import com.qiqiao.service.impl.WechatUserLoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Simon
 */

@RestController
@RequestMapping("/user/login")
public class UserLoginController {

    List<UserLoginService> userLoginServiceList;

    @PostMapping("/checkCode")
    public Result<String> loginByCheckCode(@RequestBody CheckLoginForm checkLoginForm){
        UserLoginService userLoginService = null;
        Optional<UserLoginService> optional = userLoginServiceList.stream()
                .filter(l -> {
                    return l instanceof CheckCodeUserLoginServiceImpl;
                })
                .findFirst();
        if (optional.isPresent()){
            userLoginService = optional.get();
        }
        return Objects.requireNonNull(userLoginService).login(checkLoginForm);
    }

    @PostMapping("/password")
    public Result<String> loginByPassword(@RequestBody CheckLoginForm checkLoginForm){
        UserLoginService userLoginService = null;
        Optional<UserLoginService> optional = userLoginServiceList.stream()
                .filter(l -> {
                    return l instanceof PasswordUserLoginServiceImpl;
                })
                .findFirst();
        if (optional.isPresent()){
            userLoginService = optional.get();
        }
        return Objects.requireNonNull(userLoginService).login(checkLoginForm);
    }

    @PostMapping("/wechat")
    public Result<String> loginByWechat(@RequestBody CheckLoginForm checkLoginForm){
        UserLoginService userLoginService = null;
        Optional<UserLoginService> optional = userLoginServiceList.stream()
                .filter(l -> {
                    return l instanceof WechatUserLoginServiceImpl;
                })
                .findFirst();
        if (optional.isPresent()){
            userLoginService = optional.get();
        }
        return Objects.requireNonNull(userLoginService).login(checkLoginForm);
    }


    @Autowired
    public void setUserLoginServiceList(List<UserLoginService> userLoginServiceList) {
        this.userLoginServiceList = userLoginServiceList;
    }


}
