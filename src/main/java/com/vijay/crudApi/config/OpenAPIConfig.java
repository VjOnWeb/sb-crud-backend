package com.vijay.crudApi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("CRUD API Documentation")
                .version("1.0.0")
                .description("Spring Boot CRUD API with JWT Auth, Logging, and Profiles")
                .contact(new Contact()
                    .name("Vijay")
                    .email("vijayanandvj1998@gmail.com")
                    .url("https://github.com/VjOnWeb")));
    }
}
