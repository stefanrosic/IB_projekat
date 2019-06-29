package com.bezbednost.InformacionaBezbednost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan("com.bezbednost.service")
@ComponentScan("com.bezbednost.configuration")
@ComponentScan("com.bezbednost.security")
@ComponentScan("com.bezbednost.provider")
@ComponentScan("com.bezbednost.controller")
@ComponentScan("src/main/resources")
@ComponentScan("src/main/resources/static")
@EntityScan("com.bezbednost.model")
@EnableJpaRepositories("com.bezbednost.repository")
public class InformacionaBezbednostApplication {

	public static void main(String[] args) {
		SpringApplication.run(InformacionaBezbednostApplication.class, args);

	}
}
