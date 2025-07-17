package com.vijay.crudApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudApiApplication.class, args);
	}

}

// API listening to  http://localhost:9898    - default
// API listening to  http://localhost:9899    - dev
// API listening to  http://localhost:9900    - Prod
