package com.kira.farm_fresh_store;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FarmFreshStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarmFreshStoreApplication.class, args);
	}

}
