package com.arch.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@ComponentScan(basePackages = {"com.arch.mvc.controllers"
							,"com.arch.mvc.services"
							,"com.arch.mvc.repositories"
							,"com.arch.mvc.dto"
							,"com.arch.mvc.utils"
							,"com.arch.mvc.constants"
							,"com.arch.mvc.models"})
public class MvcApplication {

	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder;
	}

	public static void main(String[] args) {
		SpringApplication.run(MvcApplication.class, args);
	}

}
