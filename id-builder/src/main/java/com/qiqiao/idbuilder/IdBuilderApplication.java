package com.qiqiao.idbuilder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author Simon
 */

@EnableDiscoveryClient
@SpringBootApplication
public class IdBuilderApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdBuilderApplication.class,args);
    }
}
