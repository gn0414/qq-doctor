package com.qiqiao.basedata;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScans;

/**
 * @author Simon
 */

@SpringBootApplication
@EnableDiscoveryClient
public class BaseDataApplication {
    public static void main(String[] args) {
        SpringApplication.run(BaseDataApplication.class,args);
    }
}

