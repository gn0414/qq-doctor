package com.qiqiao.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiqiao.model.Result;
import com.qiqiao.model.enums.ErrorEnums;
import com.qiqiao.model.finals.RedisFinals;
import com.qiqiao.model.user.domain.UserBaseInfo;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.model.user.vo.UserBaseInfoToken;
import com.qiqiao.tools.common.PasswordUtils;
import com.qiqiao.tools.user.UserTools;
import com.qiqiao.user.mapper.UserBaseInfoMapper;
import com.qiqiao.user.service.UserLoginService;
import com.qiqiao.user.util.JwtUtil;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon
 * 密码登录
 */
@Service
public class PasswordUserLoginServiceImpl implements UserLoginService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordUserLoginServiceImpl.class);

    @Value("${jwt.secret}")
    private String tokenSecret;

    @Resource
    private UserBaseInfoMapper userBaseInfoMapper;

    private final RedisTemplate<String, String> redisTemplate;



    public PasswordUserLoginServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     *
     * @param checkLoginForm 用户登录表单(包含登录方式)
     * @param response checkLoginForm,response
     * @return Result 返回结果
     */

    @SneakyThrows
    @Override
    public Result<Void> login(CheckLoginForm checkLoginForm, HttpServletResponse response) {
        //获得手机号与密码
        String phone = checkLoginForm.getPhone();
        String password = checkLoginForm.getPassword();
        //检验手机号
        if (UserTools.isErrorPhone(phone)){
            return Result.failure(
                    ErrorEnums.USER_LOGIN_CHECK_CODE_PHONE_NOT_VALID.getStatusCode(),
                    ErrorEnums.USER_LOGIN_CHECK_CODE_PHONE_NOT_VALID.getMessage());
        }
        //检验是否在线
        JwtUtil jwtUtil = new JwtUtil(tokenSecret);
        if (redisTemplate.opsForValue().get(RedisFinals.REDIS_USER_LOGIN_TOKEN+jwtUtil.generateToken(phone)) != null){
            return Result.failure(
                    ErrorEnums.USER_LOGIN_ONLINE.getStatusCode(),
                    ErrorEnums.USER_LOGIN_ONLINE.getMessage()
            );
        }
        //不在线就查库
        QueryWrapper<UserBaseInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone", phone);
        UserBaseInfo selectInfo = userBaseInfoMapper.selectOne(queryWrapper);
        if (selectInfo.getPassword() == null){
            return Result.failure(
                    ErrorEnums.USER_LOGIN_PASSWORD_ERROR.getStatusCode(),
                    ErrorEnums.USER_LOGIN_PASSWORD_ERROR.getMessage());
        }
        String saltPassword = selectInfo.getPassword();
        String checkPassword = PasswordUtils.encryptPassword(password, Base64.getDecoder().decode(selectInfo.getSalt()));
        if (!saltPassword.equals(checkPassword)){
            return Result.failure(
                    ErrorEnums.USER_LOGIN_PASSWORD_ERROR.getStatusCode(),
                    ErrorEnums.USER_LOGIN_PASSWORD_ERROR.getMessage());
        }
        UserBaseInfoToken userBaseInfoToken = new UserBaseInfoToken();
        userBaseInfoToken.setId(selectInfo.getId());
        userBaseInfoToken.setIcon(selectInfo.getIcon());
        userBaseInfoToken.setNickName(selectInfo.getNickName());
        userBaseInfoToken.setWechatNum(selectInfo.getWechatNum());
        userBaseInfoToken.setPhone(selectInfo.getPhone());
        return getVoidResult(response, phone, jwtUtil, userBaseInfoToken, redisTemplate, LOGGER);
    }

    @NotNull
    static Result<Void> getVoidResult(HttpServletResponse response, String phone, JwtUtil jwtUtil, UserBaseInfoToken userBaseInfoToken, RedisTemplate<String, String> redisTemplate, Logger logger) {
        String token;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            token = jwtUtil.generateToken(phone);
            redisTemplate.opsForValue().set(RedisFinals.REDIS_USER_LOGIN_TOKEN+token,
                    objectMapper.writeValueAsString(userBaseInfoToken),RedisFinals.REDIS_USER_LOGIN_TOKEN_EXPIRE_TIME,
                    TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            logger.error("CheckCodeUserServiceImpl:162:User Trans Json Error");
            return Result.failure(
                    ErrorEnums.INTERNET_Error.getStatusCode(),
                    ErrorEnums.INTERNET_Error.getMessage()
            );
        }
        if (token != null) {
            response.setHeader("Authorization", token);
        }
        return Result.successNoData();
    }
}
