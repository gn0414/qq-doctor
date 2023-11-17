package com.qiqiao.basedata.config;
 
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
/**
 * @author Simon
 */
@Data
@Component
@ConfigurationProperties(prefix = "mongo")
public class MongoConfigProperties {
	private String replicaSet;
	private String database;
	private String userName;
	private String password;
	private boolean enable;
	private String authDb;
	private long maxWaitTime;
}