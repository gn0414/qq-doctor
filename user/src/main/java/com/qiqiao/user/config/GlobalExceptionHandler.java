package com.qiqiao.user.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.qiqiao.model.Result;
import com.qiqiao.model.enums.ErrorEnums;
import com.qiqiao.user.exception.LoginTypeErrorException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @author Simon
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoginTypeErrorException.class)
    public Result<Void> handleLoginTypeErrorException(LoginTypeErrorException loginTypeErrorException){
        return Result.failure(ErrorEnums.USER_LOGIN_WAY_NOT_FUND.getStatusCode(), ErrorEnums.USER_LOGIN_WAY_NOT_FUND.getMessage());
    }
}