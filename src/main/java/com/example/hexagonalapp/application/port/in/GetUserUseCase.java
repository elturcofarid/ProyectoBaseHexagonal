package com.example.hexagonalapp.application.port.in;

import com.example.hexagonalapp.application.dto.GetUserQuery;
import com.example.hexagonalapp.domain.model.entity.User;

/**
 * Input port interface for the Get User use case in the Application layer.
 * This interface defines the contract for retrieving a user, allowing the infrastructure layer
 * to depend on an abstraction rather than a concrete implementation.
 */
public interface GetUserUseCase {
    User getUser(GetUserQuery query);
}