package com.example.hexagonalapp.application.port.out;

import com.example.hexagonalapp.domain.model.entity.User;
import com.example.hexagonalapp.domain.model.valueobject.UserId;

import java.util.Optional;

/**
 * Port interface for User persistence in the Application layer.
 * This output port defines the contract for user data access.
 * It allows the application layer to remain independent of specific persistence technologies.
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    boolean existsByEmail(String email);
}