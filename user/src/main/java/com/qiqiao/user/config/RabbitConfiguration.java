package com.qiqiao.user.config;
import com.qiqiao.model.finals.RabbitFinals;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author Simon
 */
@Configuration
public class RabbitConfiguration {


    @Resource
    private CachingConnectionFactory connectionFactory;

    /**
     *
     * @return 预处理一条消息的连接
     */
    @Bean(name = "listenerContainer")
    public SimpleRabbitListenerContainerFactory listenerContainerFactory(Jackson2JsonMessageConverter converter){
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        //这个是为了让每个消费者只能预消费1个消息
        factory.setPrefetchCount(1);
        factory.setMessageConverter(converter);
        return factory;
    }
    /**
     *
     */

    @Bean("jacksonConverter")
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
    /**
     * 工作队列模式
     * @return 用户信息服务发送交换机
     */
    @Bean("userMessageExchange")
    public Exchange getUserMessageExchange(){
        return ExchangeBuilder
                .directExchange(RabbitFinals.USER_MESSAGE_EXCHANGE)
                .build();
    }

    /**
     * 工作队列模式
     * @return 用户信息服务发送队列
     */
    @Bean("userMessageQueue")
    public Queue getUserMessageQueue(){
        return QueueBuilder
                .nonDurable(RabbitFinals.USER_MESSAGE_QUEUE).build();
    }


    @Bean
    public Binding getUserMessageBinding(@Qualifier("userMessageExchange") Exchange exchange,
                                         @Qualifier("userMessageQueue")Queue queue)
    {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(RabbitFinals.USER_MESSAGE_ROUTE)
                .noargs();
    }

}
