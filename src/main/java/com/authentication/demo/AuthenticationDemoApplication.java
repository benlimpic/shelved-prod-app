package com.authentication.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.authentication.demo.logger.AuthenticationLogger;

@SpringBootApplication
public class AuthenticationDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticationDemoApplication.class, args);
		AuthenticationLogger.logAuthenticationDetails();
	}

}
