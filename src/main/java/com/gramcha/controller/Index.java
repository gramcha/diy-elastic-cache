/**
 * @author gramcha
 * 11-Feb-2018 8:21:07 PM
 * 
 */
package com.gramcha.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.entities.CacheRequest;
import com.gramcha.entities.CacheResponse;
import com.gramcha.entities.RedisHost;
import com.gramcha.service.DistributionService;

@RestController
public class Index {
	@Autowired
	DistributionService distService;

	@RequestMapping(value="/cache", method=RequestMethod.GET)
	public ResponseEntity<CacheResponse> getValue(@RequestParam("key") String key){
		System.out.println("req received");
		CacheRequest req = new CacheRequest();
		req.setKey(key);
		req.setWrite(false);
		return ResponseEntity.ok(distService.processCacheReadRequest(req));
	}
	
	@RequestMapping(value="/cache", method=RequestMethod.POST)
	public ResponseEntity<CacheResponse> setValue(@RequestBody CacheRequest req){
		return ResponseEntity.ok(distService.processCacheWriteRequest(req));
	}

	@RequestMapping(value="/system", method=RequestMethod.GET)
	public ResponseEntity<List<RedisHost>> getSystemList(){
		return ResponseEntity.ok(distService.getSystemList());
	}
	@RequestMapping(value="/system", method=RequestMethod.POST)
	public ResponseEntity<String> addSystem(@RequestBody RedisHost req){
		return ResponseEntity.ok(distService.addSystem(req));
	}

	@RequestMapping(value="/system", method=RequestMethod.DELETE)
	public ResponseEntity<String> removeSystem(@RequestBody RedisHost req){
		return ResponseEntity.ok(distService.removeSystem(req));
	}
	
}
