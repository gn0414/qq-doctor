package com.qiqiao.basedata.config;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
/**
 * @author Simon
 */
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
 
	public long getMaxWaitTime() {
		return maxWaitTime;
	}
 
	public void setMaxWaitTime(long maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}
	
 
	public String getReplicaSet() {
		return replicaSet;
	}
 
	public void setReplicaSet(String replicaSet) {
		this.replicaSet = replicaSet;
	}
 
	public String getDatabase() {
		return database;
	}
 
	public void setDatabase(String database) {
		this.database = database;
	}
 
	public String getUserName() {
		return userName;
	}
 
	public void setUserName(String userName) {
		this.userName = userName;
	}
 
	public String getPassword() {
		return password;
	}
 
	public void setPassword(String password) {
		this.password = password;
	}
 
	public boolean isEnable() {
		return enable;
	}
 
	public void setEnable(boolean enable) {
		this.enable = enable;
	}
 
	public String getAuthDb() {
		return authDb;
	}
 
	public void setAuthDb(String authDb) {
		this.authDb = authDb;
	}
 
}