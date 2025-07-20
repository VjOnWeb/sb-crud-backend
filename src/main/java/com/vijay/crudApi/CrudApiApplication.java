package com.vijay.crudApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableScheduling
public class CrudApiApplication {

    public static void main(String[] args) {
        // Load .env file into system properties
        Dotenv dotenv = Dotenv.load();
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

        SpringApplication.run(CrudApiApplication.class, args);
    }
}


// API listening to  http://localhost:9898    - dev
// API listening to  http://localhost:9899    - Prod
