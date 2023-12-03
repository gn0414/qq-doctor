package com.qiqiao.user.config;

import com.netflix.client.ClientException;
import com.qiqiao.model.Result;
import com.qiqiao.model.enums.ErrorEnums;
import com.qiqiao.user.exception.LoginTypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLIntegrityConstraintViolationException;


/**
 * @author Simon
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(LoginTypeErrorException.class)
    public Result<Void> handleLoginTypeErrorException(LoginTypeErrorException loginTypeErrorException){
        return Result.failure(ErrorEnums.USER_LOGIN_WAY_NOT_FUND.getStatusCode(), ErrorEnums.USER_LOGIN_WAY_NOT_FUND.getMessage());
    }

    @ExceptionHandler(ClientException.class)
    public Result<Void> handleClientException(ClientException clientException){
        LOGGER.error(clientException.getErrorMessage());
        return Result.failure(ErrorEnums.INTERNET_Error.getStatusCode(), ErrorEnums.INTERNET_Error.getMessage());
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<Void> handleSqlIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException sqlIntegrityConstraintViolationException){
        LOGGER.error(sqlIntegrityConstraintViolationException.getMessage());
        return Result.failure(ErrorEnums.INTERNET_Error.getStatusCode(), ErrorEnums.INTERNET_Error.getMessage());
    }

    @ExceptionHandler(NoSuchAlgorithmException.class)
    public Result<Void> handleNoSuchAlgorithmException(NoSuchAlgorithmException noSuchAlgorithmException){
        LOGGER.error(noSuchAlgorithmException.getMessage());
        return Result.failure(ErrorEnums.USER_LOGIN_PASSWORD_ERROR.getStatusCode(), ErrorEnums.USER_LOGIN_PASSWORD_ERROR.getMessage());
    }
}