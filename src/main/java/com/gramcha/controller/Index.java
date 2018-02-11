/**
 * @author gramcha
 * 11-Feb-2018 8:21:07 PM
 * 
 */
package com.gramcha.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gramcha.service.DistributionService;

@RestController
public class Index {
	@Autowired
	DistributionService distService;
	@RequestMapping(value="/")
	public ResponseEntity<String> ping() throws Exception{
		return ResponseEntity.ok("pong - "+distService.test());
	}
}
