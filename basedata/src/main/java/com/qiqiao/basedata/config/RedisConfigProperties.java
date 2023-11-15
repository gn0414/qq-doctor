package com.qiqiao.basedata.config;
 
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
 
/**
 * @author Simon
 */
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
 

 

 

 
	public String getHost() {
		return host;
	}
 
	public void setHost(String host) {
		this.host = host;
	}
 
	public int getPort() {
		return port;
	}
 
	public void setPort(int port) {
		this.port = port;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}
 
	public int getMaxIdle() {
		return maxIdle;
	}
 
	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}
 
	public int getMinIdle() {
		return minIdle;
	}
 
	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}
 
	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}
 
	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
 
	public int getTimeout() {
		return timeout;
	}
 
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
 
	public int getDatabase() {
		return database;
	}
 
	public void setDatabase(int database) {
		this.database = database;
	}
 
	public int getMaxTotal() {
		return maxTotal;
	}
 
	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}
 
}