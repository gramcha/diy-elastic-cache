/**
 * @author gramcha
 * 13-Feb-2018 2:43:19 PM
 * 
 */
package com.gramcha.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gramcha.entities.RedisHost;

import redis.clients.jedis.Jedis;

@Service
public class RedisServiceManager {
	private Map<String, Jedis> redisInstanceMap = new HashMap<>();

	public <T> void createConnection(T node) {
		RedisHost redisHost = (RedisHost) node;
		redisInstanceMap.put(redisHost.toString(), openConnection(redisHost));
	}

	public <T> void removeConnection(T node) {
		RedisHost redisHost = (RedisHost) node;
		closeConnection(redisHost);
		redisInstanceMap.remove(redisHost.toString());
	}

	private Jedis openConnection(RedisHost redisHost) {
		Jedis jedis = new Jedis(redisHost.getHost(), redisHost.getPortNumber());// http://www.baeldung.com/jedis-java-redis-client-library
		System.out.println("connection opened for redishost " + redisHost.toString());
		return jedis;
	}

	private void closeConnection(RedisHost redisHost) {
		Jedis instance = redisInstanceMap.get(redisHost.toString());
		if (instance != null) {
			instance.close();
		}
	}

	public void set(RedisHost redisHost, String key, String value) {
		System.out.println("redishost " + redisHost.toString());
		Jedis instance = redisInstanceMap.get(redisHost.toString());
		if (instance != null) {
			instance.set(key, value);
		}
	}

	public String get(RedisHost redisHost, String key) {
		Jedis instance = redisInstanceMap.get(redisHost.toString());
		if (instance != null) {
			return instance.get(key);
		}
		return null;
	}

	public List<RedisHost> getInstanceList() {
		List<String> keyList = redisInstanceMap.keySet().stream().collect(Collectors.toList());
		List<RedisHost> sysList =  new ArrayList<>();
		keyList.stream().forEach(item->{
			String[] sItems = item.split(":");
			sysList.add(new RedisHost(sItems[0], sItems[1]));
		});
		return sysList;
	}
}
