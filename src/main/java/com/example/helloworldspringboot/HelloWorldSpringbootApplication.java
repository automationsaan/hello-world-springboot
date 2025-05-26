package com.example.helloworldspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Marks this as a Spring Boot application and enables auto-configuration
public class HelloWorldSpringbootApplication {
    public static void main(String[] args) {
        // Entry point: launches the Spring Boot application
        SpringApplication.run(HelloWorldSpringbootApplication.class, args);
    }
}
