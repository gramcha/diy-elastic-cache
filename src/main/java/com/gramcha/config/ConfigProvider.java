/**
 * @author gramcha
 * 11-Feb-2018 8:21:17 PM
 * 
 */
package com.gramcha.config;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.gramcha.entities.RedisHost;

@Component
@ConfigurationProperties
public class ConfigProvider {
	@NotNull
	private List<RedisHost> redisInstances;

	@NotNull
	private String numberOfReplicas;
	
	public List<RedisHost> getRedisInstances() {
		return redisInstances;
	}
	
	public void setRedisInstances(List<RedisHost> redisInstances) {
		this.redisInstances = redisInstances;
	}

	public int getNumberOfReplicas() {
		return Integer.parseInt(numberOfReplicas);
	}

	public void setNumberOfReplicas(String numberOfReplicas) {
		this.numberOfReplicas = numberOfReplicas;
	}
	
}