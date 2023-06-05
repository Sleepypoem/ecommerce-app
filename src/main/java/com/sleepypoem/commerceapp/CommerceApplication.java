package com.sleepypoem.commerceapp;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class CommerceApplication {

    public static void main(String[] args) {

        SpringApplication.run(CommerceApplication.class, args);
    }


}
