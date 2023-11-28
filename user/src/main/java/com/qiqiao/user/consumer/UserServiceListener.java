package com.qiqiao.user.consumer;

import com.qiqiao.model.finals.RabbitFinals;
import com.qiqiao.model.messagqueue.MessageVo;
import com.qiqiao.tools.common.MessageTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author Simon
 */

@Component
public class UserServiceListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceListener.class);
    /**
     * concurrency 表示消费者数，如果是10个消费者就将轮询10个线程来执行消费业务
     *
     */

    @RabbitListener(queues = RabbitFinals.USER_MESSAGE_QUEUE, concurrency = "10",containerFactory = "listenerContainer")
    public void userSendMessageReceiver(MessageVo messageVo){
        //发送短信
        try {
            MessageTools.sendMessage(messageVo.getPhone(), messageVo.getCode(), messageVo.getId(), messageVo.getSecret());
        }catch (Exception e){
            LOGGER.error(messageVo.getPhone()+"：调用阿里云服务失败");
        }
    }

}
