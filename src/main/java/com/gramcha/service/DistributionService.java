/**
 * @author gramcha
 * 11-Feb-2018 8:23:28 PM
 * 
 */
package com.gramcha.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gramcha.config.ConfigProvider;

import redis.clients.jedis.Jedis;

@Service
public class DistributionService {
	@Autowired
	ConfigProvider config;
	
	private List<Jedis> redisInstances = new ArrayList<>();
	@PostConstruct
	void init() {
		System.out.println("connecting with redis cache instances");
		config.getRedisInstances().forEach(item->{
			Jedis jedis = new Jedis(item.getHost(), Integer.parseInt(item.getPort()));//http://www.baeldung.com/jedis-java-redis-client-library
			System.out.println("connection created with "+item);
			redisInstances.add(jedis);
		});
	}
	
	public String test() {
		return redisInstances.get(0).get("events/city/rome")+" "+redisInstances.get(1).get("events/city/rome");
	}
}

//https://stackoverflow.com/questions/12362417/same-consistent-hashing-algorithm-implementation-for-java-and-python-program
//consistant hashing with bounded-load
//https://github.com/lafikl/consistent
