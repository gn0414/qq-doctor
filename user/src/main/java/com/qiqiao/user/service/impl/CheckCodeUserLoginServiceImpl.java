package com.qiqiao.user.service.impl;

import com.qiqiao.model.Result;
import com.qiqiao.model.enums.ErrorEnums;
import com.qiqiao.model.finals.RabbitFinals;
import com.qiqiao.model.finals.RedisFinals;
import com.qiqiao.model.messagqueue.MessageVo;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.tools.common.VerificationCodeGenerator;
import com.qiqiao.tools.user.UserTools;
import com.qiqiao.user.service.UserLoginService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon
 * 验证码登录
 * 验证码就只有这个方式会用我就不抽象成接口了
 */
@Service
public class CheckCodeUserLoginServiceImpl implements UserLoginService{
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckCodeUserLoginServiceImpl.class);
    private static final int SEND_MSG_MAX_TIME = 30;
    private static final int IP_MAX_NUM = 10;

    @Value(("${ALIBABA_CLOUD_ACCESS_KEY_ID}"))
    private String id;

    @Value("${ALIBABA_CLOUD_ACCESS_KEY_SECRET}")
    private String secret;

    @Resource
    private  RabbitTemplate rabbitTemplate;

    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;

    public CheckCodeUserLoginServiceImpl(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public Result<Void> login(CheckLoginForm checkLoginForm) {
        return Result.successNoData();
    }

    /**
     *第三方api调用太慢了所以仍消息队列
     * @param phone 手机号
     * @return 发送结果
     */
    public Result<Void> sendCheckCode(String phone, HttpServletRequest request){
        //校验手机号的合理性
        if (!UserTools.isValidPhone(phone)) {
            return Result.failure(
                    ErrorEnums.USER_LOGIN_CHECK_CODE_PHONE_NOT_VALID.getStatusCode(),
                    ErrorEnums.USER_LOGIN_CHECK_CODE_PHONE_NOT_VALID.getMessage());
        }
        //校验是否已经存在该手机号的验证码
        String phoneValueKey = RedisFinals.REDIS_USER_CHECK_CODE_VALUE+phone;
        String phoneValueValue = redisTemplate.opsForValue().get(phoneValueKey);
        if (phoneValueValue != null){
            return Result.failure(
                    ErrorEnums.USER_LOGIN_CHECK_CODE_EXPIRE.getStatusCode(),
                    ErrorEnums.USER_LOGIN_CHECK_CODE_EXPIRE.getMessage());
        }
        //校验ip24h发送短信数(去redis)
        String ipNumKey = RedisFinals.REDIS_USER_CHECK_CODE_NUM+ getIpAddress(request);
        String ipNumValue = redisTemplate.opsForValue().get(ipNumKey);
        if (ipNumValue == null || Integer.parseInt(ipNumValue) < IP_MAX_NUM){
            //获取ip锁
            RLock lock = redissonClient.getLock(RedisFinals.REDISSON_USER_CHECK_CODE_LOCK + getIpAddress(request));
            lock.lock(SEND_MSG_MAX_TIME, TimeUnit.SECONDS);
            //双检
            ipNumValue = redisTemplate.opsForValue().get(ipNumKey);
            if (ipNumValue == null || Integer.parseInt(ipNumValue) < IP_MAX_NUM){
                String code = VerificationCodeGenerator.generateVerificationCode();
                //发送给指定队列去消费
                rabbitTemplate.convertAndSend(RabbitFinals.USER_MESSAGE_EXCHANGE,
                        RabbitFinals.USER_MESSAGE_ROUTE,
                        new MessageVo(phone,code,id,secret));
                //发送完毕后写入redis
                redisTemplate.opsForValue().set(phoneValueKey,code,RedisFinals
                        .REDIS_USER_CHECK_CODE_EXPIRE,TimeUnit.SECONDS);
                if (ipNumValue == null){
                    redisTemplate.opsForValue().set(ipNumKey,"1",RedisFinals.REDIS_USER_CHECK_CODE_NUM_EXPIRE,TimeUnit.SECONDS);
                }else{
                    ipNumValue= String.valueOf(Integer.parseInt(ipNumValue)+1);
                    redisTemplate.opsForValue().set(ipNumKey,ipNumValue,
                            RedisFinals.REDIS_USER_CHECK_CODE_NUM_EXPIRE,TimeUnit.SECONDS);
                }
            }else{
                if (lock.isLocked()){
                    lock.unlock();
                }
                return Result.failure(
                        ErrorEnums.USER_LOGIN_CHECK_CODE_IP_MAX.getStatusCode(),
                        ErrorEnums.USER_LOGIN_CHECK_CODE_IP_MAX.getMessage());
            }
            if (lock.isLocked()){
                lock.unlock();
            }
            return Result.successNoData();
        }
        //返回24h内同一IP短信量超标
        return Result.failure(
                ErrorEnums.USER_LOGIN_CHECK_CODE_IP_MAX.getStatusCode(),
                ErrorEnums.USER_LOGIN_CHECK_CODE_IP_MAX.getMessage());
    }

    private static String getIpAddress(HttpServletRequest request){
        //从请求头获取IP
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null){
            //没有就从请求体
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
