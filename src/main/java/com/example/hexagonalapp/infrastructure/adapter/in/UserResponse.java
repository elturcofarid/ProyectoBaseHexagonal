package com.example.hexagonalapp.infrastructure.adapter.in;

/**
 * DTO for User response in REST API.
 * Separates the external representation from the internal domain model.
 */
public class UserResponse {
    private Long id;
    private String name;
    private String email;

    public UserResponse() {}

    public UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}