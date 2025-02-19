package com.kira.farm_fresh_store;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@EnableJpaAuditing
@EnableProcessApplication
public class FarmFreshStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmFreshStoreApplication.class, args);
	}

}
