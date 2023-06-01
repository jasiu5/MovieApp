package com.example.UserAuthService;

import com.example.UserAuthService.Model.User;
import com.example.UserAuthService.Repository.UserRepository;
import com.example.UserAuthService.Service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class UserAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserAuthServiceApplication.class, args);

	}

}
