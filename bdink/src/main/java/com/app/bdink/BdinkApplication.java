package com.app.bdink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BdinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(BdinkApplication.class, args);
	}

}
