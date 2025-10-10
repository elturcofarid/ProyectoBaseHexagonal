package com.example.hexagonalapp.infrastructure.adapter.out;

import com.example.hexagonalapp.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository interface for UserEntity in the Infrastructure layer.
 * This interface provides basic CRUD operations and custom queries.
 * It leverages Spring Data JPA to reduce boilerplate code.
 */
@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);
}