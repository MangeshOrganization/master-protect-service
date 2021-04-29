package com.challange.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
public class MasterProtectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MasterProtectApplication.class, args);
	}

}
