package com.example.helloworldspringboot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest // Indicates that this is a Spring Boot test that will start the application context
class HelloWorldSpringbootApplicationTests {
    @Test // Marks this method as a test case
    void contextLoads() {
        // Simple test to check if the Spring application context loads successfully
        assertThat(true).isTrue();
    }
}
