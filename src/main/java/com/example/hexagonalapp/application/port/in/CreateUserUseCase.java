package com.example.hexagonalapp.application.port.in;

import com.example.hexagonalapp.domain.model.entity.User;

/**
 * Input port interface for the Create User use case in the Application layer.
 * This interface defines the contract for creating a user, allowing the infrastructure layer
 * to depend on an abstraction rather than a concrete implementation.
 */
public interface CreateUserUseCase {
    User createUser(String name, String email);
}