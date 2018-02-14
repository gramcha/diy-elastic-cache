/**
 * @author gramcha
 * 11-Feb-2018 8:23:28 PM
 * 
 */
package com.gramcha.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gramcha.config.ConfigProvider;
import com.gramcha.entities.CacheRequest;
import com.gramcha.entities.CacheResponse;
import com.gramcha.entities.RedisHost;

import redis.clients.jedis.Jedis;

@Service
public class DistributionService {
	@Autowired
	ConfigProvider config;

	@Autowired
	RedisServiceManager redisService;

	@Autowired
	ConsistentHashingService<RedisHost> consistentHashService;

	@PostConstruct
	void init() {
		System.out.println("connecting with redis cache instances");
	}

	public String test() throws IOException {
		RedisHost redisHost = (RedisHost) consistentHashService.get("events/city/rome");
		// redisService.set(redisHost, "events/city/rome", (new Date()).toString());
		return redisService.get(redisHost, "events/city/rome");
	}

	public CacheResponse processCacheReadRequest(CacheRequest req) {
		CacheResponse resp = new CacheResponse();
		try {
			RedisHost cacheServer = consistentHashService.get(req.getKey());
			resp.setKey(req.getKey());
			resp.setValue(redisService.get(cacheServer, req.getKey()));
			resp.setStatus("retrieved from " + cacheServer.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setStatus("error while fetching from cache...");
		}
		return resp;
	}

	public CacheResponse processCacheWriteRequest(CacheRequest req) {
		CacheResponse resp = new CacheResponse();
		try {
			RedisHost cacheServer = consistentHashService.get(req.getKey());
			redisService.set(cacheServer, req.getKey(), req.getValue());
			resp.setKey(req.getKey());
			resp.setValue(req.getValue());
			resp.setStatus("cached in " + cacheServer.toString());
			System.out.println(redisService.get(cacheServer, req.getKey()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resp.setStatus("error while caching...");
		}
		return resp;
	}

	/**
	 * @param req
	 * @return
	 */
	public String addSystem(RedisHost req) {
		List<RedisHost> nodes = new ArrayList<>();
		nodes.add(req);
		try {
			consistentHashService.addNodes(nodes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "given cache system not added due to " + e.getMessage();
		}
		return "System " + req.toString() + " " + "added successfully.";
	}

	/**
	 * @param req
	 * @return
	 */
	public String removeSystem(RedisHost req) {
		List<RedisHost> nodes = new ArrayList<>();
		nodes.add(req);
		try {
			consistentHashService.removeNodes(nodes);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "given cache system not removed due to " + e.getMessage();
		}
		return "System " + req.toString() + " " + "removed successfully.";
	}

	/**
	 * @return
	 */
	public List<RedisHost> getSystemList() {
		// TODO Auto-generated method stub
		return redisService.getInstanceList();
	}
}

// https://stackoverflow.com/questions/12362417/same-consistent-hashing-algorithm-implementation-for-java-and-python-program
// consistant hashing with bounded-load
// https://github.com/lafikl/consistent
