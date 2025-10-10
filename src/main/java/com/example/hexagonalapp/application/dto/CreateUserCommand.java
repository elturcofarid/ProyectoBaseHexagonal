package com.example.hexagonalapp.application.dto;

/**
 * Command DTO for creating a User in the Application layer.
 * This represents the input port for the create user use case.
 * It defines the data required to create a new user.
 */
public class CreateUserCommand {
    private final String name;
    private final String email;

    public CreateUserCommand(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}