package com.qiqiao.basedata.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;


/**
 * @author Simon
 */
@Configuration
public class CaffeineConfig {
    @Bean
    public Cache<String,Object> localCache(){
        return Caffeine.newBuilder()
                .initialCapacity(0)
                .maximumSize(4)
                .build();
    }
}
