package com.bank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(title = "The Banking Application",
		description = "Backend Rest APIs",
		contact = @Contact(email = "jgaurav117@gmail.com",name = "Gaurav Jain",url = "https://www.linkedin.com/in/gjain87"),
		version = "V1.0.0",
		summary = "This is a backend Spring Application for Banking services.."
		)
)
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

}
