package com.qiqiao.user.controller;

import com.qiqiao.model.Result;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.user.exception.LoginTypeErrorException;
import com.qiqiao.user.service.UserLoginService;
import com.qiqiao.user.service.impl.CheckCodeUserLoginServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Simon
 */

@RestController
@RequestMapping("/user")
public class UserLoginController {

    private final List<UserLoginService> userLoginServiceList;



    private final CheckCodeUserLoginServiceImpl checkCodeUserLoginService;




    @PostMapping("/login")
    public Result<Void> login(@RequestBody CheckLoginForm checkLoginForm) {
        UserLoginService userLoginService = userLoginServiceList.stream()
                .filter(service -> service.getClass().getSimpleName()
                        .substring(0,service.getClass().getSimpleName().length()-20).equals(checkLoginForm.getType()))
                .findFirst()
                .orElseThrow(LoginTypeErrorException::new);
        return userLoginService.login(checkLoginForm);
    }

    @GetMapping("/checkCode")
    public Result<Void> getCheckCode(String phone, HttpServletRequest request){
        return checkCodeUserLoginService.sendCheckCode(phone,request);
    }

    public UserLoginController(List<UserLoginService> userLoginServiceList, CheckCodeUserLoginServiceImpl checkCodeUserLoginService) {
        this.userLoginServiceList = userLoginServiceList;
        this.checkCodeUserLoginService = checkCodeUserLoginService;
    }

}
