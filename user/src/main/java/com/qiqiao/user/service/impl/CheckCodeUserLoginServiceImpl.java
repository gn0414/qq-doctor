package com.qiqiao.user.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qiqiao.model.Result;
import com.qiqiao.model.enums.ErrorEnums;
import com.qiqiao.model.finals.CommonFinals;
import com.qiqiao.model.finals.RabbitFinals;
import com.qiqiao.model.finals.RedisFinals;
import com.qiqiao.model.messagqueue.MessageVo;
import com.qiqiao.model.user.domain.UserBaseInfo;
import com.qiqiao.model.user.vo.CheckLoginForm;
import com.qiqiao.model.user.vo.UserBaseInfoToken;
import com.qiqiao.server.idbuilder.IdBuilderFeignClient;
import com.qiqiao.tools.common.PasswordUtils;
import com.qiqiao.tools.common.RandomNameGenerator;
import com.qiqiao.tools.common.VerificationCodeGenerator;
import com.qiqiao.tools.user.UserTools;
import com.qiqiao.user.mapper.UserBaseInfoMapper;
import com.qiqiao.user.service.UserLoginService;
import com.qiqiao.user.util.JwtUtil;
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
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.qiqiao.user.service.impl.PasswordUserLoginServiceImpl.getVoidResult;

/**
 * @author Simon
 * 验证码登录
 * 验证码就只有这个方式会用我就不抽象成接口了
 */
@Service
public class CheckCodeUserLoginServiceImpl implements UserLoginService{

    @Value("${jwt.secret}")
    private String tokenSecret;


    private static final Logger LOGGER = LoggerFactory.getLogger(CheckCodeUserLoginServiceImpl.class);
    private static final int SEND_MSG_MAX_TIME = 30;
    private static final int IP_MAX_NUM = 10;

    @Value(("${ALIBABA_CLOUD_ACCESS_KEY_ID}"))
    private String id;

    @Value("${ALIBABA_CLOUD_ACCESS_KEY_SECRET}")
    private String secret;

    @Resource
    private  RabbitTemplate rabbitTemplate;

    @Resource
    private UserBaseInfoMapper userBaseInfoMapper;

    @Resource
    private IdBuilderFeignClient idBuilderFeignClient;


    private final RedisTemplate<String, String> redisTemplate;

    private final RedissonClient redissonClient;


    public CheckCodeUserLoginServiceImpl(RedisTemplate<String, String> redisTemplate, RedissonClient redissonClient) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
    }

    @Override
    public Result<Void> login(CheckLoginForm checkLoginForm, HttpServletResponse response) {
        String phone = checkLoginForm.getPhone();
        String checkCode = checkLoginForm.getCheckCode();
        //校验手机号
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
        //检验验证码是否正确
        if (UserTools.isErrorCheckCode(checkCode)){
            return Result.failure(
                    ErrorEnums.USER_LOGIN_CHECK_CODE_NOT_VALID.getStatusCode(),
                    ErrorEnums.USER_LOGIN_CHECK_CODE_NOT_VALID.getMessage()
            );
        }
        //
        String phoneValueKey = RedisFinals.REDIS_USER_CHECK_CODE_VALUE+phone;
        String phoneValueValue = redisTemplate.opsForValue().get(phoneValueKey);
        String token = null;
        if (checkCode.equals(phoneValueValue)){
            //校验成功先查数据库是否有该手机号记录
            // 查询已删除或未删除状态的用户
            QueryWrapper<UserBaseInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("phone", phone);
            UserBaseInfo selectInfo = userBaseInfoMapper.selectOne(queryWrapper);
            UserBaseInfoToken userBaseInfoToken = new UserBaseInfoToken();
            if (selectInfo == null){
                //封装一个UserBaseInfo基础数据
                //TODO 后面还会有具体信息表,所以后面还会插入一个具体信息表
                UserBaseInfo userBaseInfo = new UserBaseInfo();
                //远程调用feignClient
                Result<Long> buildId = idBuilderFeignClient.getBuildId();
                //获取加密盐值
                byte[] secret = null;
                try{
                     secret = PasswordUtils.generateSalt();
                }catch (NoSuchAlgorithmException e){
                    LOGGER.error("CheckCodeUserServiceImpl:117:Secret Create Error");
                }
                String saltString = Base64.getEncoder().encodeToString(secret);
                Long id = buildId.getData();
                userBaseInfoToken.setId(id);
                userBaseInfoToken.setPhone(phone);
                userBaseInfoToken.setNickName("QQYJ_"+ RandomNameGenerator.generateRandomName());
                userBaseInfo.setId(id);
                userBaseInfo.setPhone(phone);
                userBaseInfo.setNickName("QQYJ_"+ RandomNameGenerator.generateRandomName());
                userBaseInfo.setSalt(saltString);
                //插入数据库
                userBaseInfoMapper.insert(userBaseInfo);
            }else{
                if (selectInfo.getIsBan().equals(CommonFinals.IS_BAN)){
                    return Result.failure(
                            ErrorEnums.USER_LOGIN_BAN.getStatusCode(),
                            ErrorEnums.USER_LOGIN_BAN.getMessage()
                    );
                }
                    userBaseInfoToken.setId(selectInfo.getId());
                    userBaseInfoToken.setIcon(selectInfo.getIcon());
                    userBaseInfoToken.setNickName(selectInfo.getNickName());
                    userBaseInfoToken.setWechatNum(selectInfo.getWechatNum());
                    userBaseInfoToken.setPhone(selectInfo.getPhone());

            }
            //Token数据体写完就写入redis
            return getVoidResult(response, phone, jwtUtil, userBaseInfoToken, redisTemplate, LOGGER);
        }
        return Result.failure(
                ErrorEnums.USER_LOGIN_CHECK_CODE_FAIL.getStatusCode(),
                ErrorEnums.USER_LOGIN_CHECK_CODE_FAIL.getMessage()
        );
    }

    /**
     *第三方api调用太慢了所以仍消息队列
     * @param phone 手机号
     * @return 发送结果
     */
    public Result<Void> sendCheckCode(String phone, HttpServletRequest request){
        //校验手机号的合理性
        if (phone == null || UserTools.isErrorPhone(phone)) {
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
        Long ipExpire = redisTemplate.getExpire(ipNumKey);
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
                            Optional.ofNullable(ipExpire).orElse((long) RedisFinals.REDIS_USER_CHECK_CODE_NUM_EXPIRE),TimeUnit.SECONDS
                    );
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
