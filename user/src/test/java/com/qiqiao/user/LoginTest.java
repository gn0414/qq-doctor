package com.qiqiao.user;
import com.qiqiao.server.idbuilder.IdBuilderFeignClient;
import com.qiqiao.tools.common.MessageTools;
import com.qiqiao.tools.common.PasswordUtils;
import com.qiqiao.user.mapper.UserBaseInfoMapper;
import com.qiqiao.user.service.UserLoginService;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;


import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class LoginTest {


    @Autowired
    IdBuilderFeignClient idBuilderFeignClient;

    @Autowired
    RedissonClient redissonClient;

    @Value(("${ALIBABA_CLOUD_ACCESS_KEY_ID}"))
    private String id;

    @Value("${ALIBABA_CLOUD_ACCESS_KEY_SECRET}")
    private String secret;
    @Autowired
    List<UserLoginService> userLoginServiceList;
    @Autowired
    UserBaseInfoMapper userBaseInfoMapper;

    @Test
    void TestFeign(){
        System.out.println(idBuilderFeignClient.getBuildId());
    }

    @Test
    void TestClassName(){
        userLoginServiceList.forEach(
                l->{
                    System.out.println(l.getClass().getSimpleName()
                            .substring(0,l.getClass().getSimpleName().length()-20));
                }
        );
    }


    @Test
    void TestSendMessage(){
        try {
            MessageTools.sendMessage("18683927916","665124",id,secret);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void TestRedisson() throws InterruptedException {
        AtomicInteger testNum = new AtomicInteger(10);

        CountDownLatch latch = new CountDownLatch(10);

        RLock testLock = redissonClient.getLock("testLock");

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                testLock.lock();
                try {
                    testNum.getAndDecrement();
                    System.out.println(Thread.currentThread().getName() + "获取一个数");
                } finally {
                    testLock.unlock();
                    latch.countDown();
                }
            }).start();
        }

        latch.await();

        System.out.println("最终结果：" + testNum.get());
    }

    @Test
    void PasswordCheck() throws NoSuchAlgorithmException {
        String password = "simon";
        byte[] salt = PasswordUtils.generateSalt();
        String dataPassword = PasswordUtils.encryptPassword(password, salt);
        String saltTrans = Base64.getEncoder().encodeToString(salt);
        byte[] decodeSalt = Base64.getDecoder().decode(saltTrans);
        String checkPassword = PasswordUtils.encryptPassword(password,decodeSalt);

        System.out.println(dataPassword);
        System.out.println(checkPassword);
        System.out.println(dataPassword.equals(checkPassword));

    }

}
