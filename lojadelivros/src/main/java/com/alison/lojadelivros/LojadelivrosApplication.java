package com.alison.lojadelivros;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.alison.lojadelivros.model")
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = { "com.alison.lojadelivros.repository" })
public class LojadelivrosApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojadelivrosApplication.class, args);
	}

}
