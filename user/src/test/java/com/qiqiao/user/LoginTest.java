package com.qiqiao.user;


import com.qiqiao.user.service.UserLoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class LoginTest {
    @Autowired
    List<UserLoginService> userLoginServiceList;
    @Test
    void TestClassName(){
        userLoginServiceList.forEach(
                l->{
                    System.out.println(l.getClass().getSimpleName()
                            .substring(0,l.getClass().getSimpleName().length()-20));
                }
        );
    }
}
