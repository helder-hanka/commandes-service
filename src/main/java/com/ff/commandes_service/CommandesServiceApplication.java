package com.ff.commandes_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class CommandesServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandesServiceApplication.class, args);
	}

}
