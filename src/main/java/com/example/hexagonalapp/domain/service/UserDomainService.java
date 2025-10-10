package com.example.hexagonalapp.domain.service;

import com.example.hexagonalapp.domain.model.entity.User;
import com.example.hexagonalapp.domain.model.valueobject.EmailAddress;

/**
 * DomainService that encapsulates business logic not belonging to a single entity.
 * This service handles domain rules that involve multiple entities or complex validations.
 * It remains framework-independent and focuses on pure domain logic.
 */
public class UserDomainService {

    /**
     * Validates if a user can be created with the given email.
     * This is domain logic that could involve checking against business rules.
     */
    public boolean canCreateUserWithEmail(User existingUser, EmailAddress newEmail) {
        if (existingUser == null) {
            return true; // No existing user, can create
        }
        // Business rule: cannot create user with same email as existing user
        return !existingUser.getEmail().equals(newEmail);
    }

    /**
     * Checks if a user is valid for business operations.
     * This could include complex domain validations.
     */
    public boolean isUserValidForOperations(User user) {
        // Example business rule: user must have a name and valid email
        return user.getName() != null &&
               user.getEmail() != null &&
               user.getName().getValue().length() >= 2;
    }
}