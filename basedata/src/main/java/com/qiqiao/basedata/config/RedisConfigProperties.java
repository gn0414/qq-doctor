package com.qiqiao.basedata.config;
 
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
/**
 * @author Simon
 */
@Data
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfigProperties {
	private String host;
	private int port;
	private String pass;
	private int maxIdle;
	private int minIdle;
	private boolean testOnBorrow;
	private int timeout;
	private int database;
	protected int maxTotal;
 
}