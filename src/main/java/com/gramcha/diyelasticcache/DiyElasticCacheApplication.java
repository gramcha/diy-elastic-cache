package com.gramcha.diyelasticcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.gramcha.*"})
public class DiyElasticCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiyElasticCacheApplication.class, args);
	}
}
