package com.quiroz.mypayments.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("My Personal Finance API")
                        .description(
                                "Personal Finance project implemented with Spring Boot and Java 21.")
                        .contact(new Contact()
                                .name("Ariel Quiroz")
                                .url("https://github.com/senoritadeveloper01")));
    }
}

