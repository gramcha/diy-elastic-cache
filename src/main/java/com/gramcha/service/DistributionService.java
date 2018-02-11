/**
 * @author gramcha
 * 11-Feb-2018 8:23:28 PM
 * 
 */
package com.gramcha.service;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import redis.clients.jedis.Jedis;

@Service
public class DistributionService {
	Jedis jedis = null;
	Jedis jedis2 = null;
	@PostConstruct
	void init() {
		System.out.println("initilizing redis cache part");
		jedis = new Jedis();
		System.out.println("jedis 1 = "+jedis.toString());
		jedis2 = new Jedis("127.0.0.1", 6380);
		
		jedis2.set("events/city/rome", "32,15,223,828-instance2");
	}
	
	public String test() {
		return jedis.get("events/city/rome")+" "+jedis2.get("events/city/rome");
	}
}
