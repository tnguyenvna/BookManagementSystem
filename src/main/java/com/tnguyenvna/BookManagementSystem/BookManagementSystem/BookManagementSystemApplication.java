package com.tnguyenvna.BookManagementSystem.BookManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;


@OpenAPIDefinition(
	info = @Info(
		title = "BOOK MANAGERMENT REST APIs",
		description = "BOOK MANAGEMENT REST API ENDPOINTS DOUMENTTATIONS",
		version = "v1.0.0",
		contact = @Contact(
			name = "Tnguyen",
			email = "info@tnguyen.com",
			url = "http...."
		),
		license = @License(
			name= "Apache 2.0",
			url = "http..."
		)
	),
	externalDocs = @ExternalDocumentation(
		description = "BOOK MANAGEMENT REST API ENDPOINTS DOCUMENTTATIONS",
		url = "http..."
	)
)
@SpringBootApplication
public class BookManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookManagementSystemApplication.class, args);
	}

}
