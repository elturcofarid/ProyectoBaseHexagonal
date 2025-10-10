package com.example.hexagonalapp.application.dto;

import com.example.hexagonalapp.domain.model.valueobject.UserId;

/**
 * Query DTO for retrieving a User in the Application layer.
 * This represents the input port for the get user use case.
 * It defines the data required to query a user by ID.
 */
public class GetUserQuery {
    private final UserId userId;

    public GetUserQuery(UserId userId) {
        this.userId = userId;
    }

    public UserId getUserId() {
        return userId;
    }
}