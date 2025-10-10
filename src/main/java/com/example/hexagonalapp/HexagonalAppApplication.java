package com.example.hexagonalapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Hexagonal Architecture Spring Boot application.
 * This class bootstraps the Spring Boot application and enables auto-configuration.
 * It serves as the entry point for the Infrastructure layer.
 */
@SpringBootApplication
public class HexagonalAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(HexagonalAppApplication.class, args);
    }
}