package com.qiqiao.user.controller;

import com.qiqiao.model.Result;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.user.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Simon
 */

@RestController
@RequestMapping("/user")
public class UserLoginController {

    List<UserLoginService> userLoginServiceList;

    @PostMapping("/login")
    public Result<String> login(@RequestBody CheckLoginForm checkLoginForm) {
        UserLoginService userLoginService = userLoginServiceList.stream()
                .filter(service -> service.getClass().getSimpleName()
                        .substring(0,service.getClass().getSimpleName().length()-20).equals(checkLoginForm.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No UserLoginService available for type: " + checkLoginForm.getType()));
        return userLoginService.login(checkLoginForm);
    }
    @Autowired
    public void setUserLoginServiceList(List<UserLoginService> userLoginServiceList) {
        this.userLoginServiceList = userLoginServiceList;
    }
}
