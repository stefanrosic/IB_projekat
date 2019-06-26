package com.example.InformacionaBezbednost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableAutoConfiguration
@SpringBootApplication
@EntityScan("com.bezbednost.model")
public class InformacionaBezbednostApplication {

	public static void main(String[] args) {
		SpringApplication.run(InformacionaBezbednostApplication.class, args);
	}
}
