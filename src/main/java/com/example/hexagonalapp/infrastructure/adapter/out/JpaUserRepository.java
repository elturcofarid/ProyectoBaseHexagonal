package com.example.hexagonalapp.infrastructure.adapter.out;

import com.example.hexagonalapp.application.port.out.UserRepository;
import com.example.hexagonalapp.domain.model.entity.User;
import com.example.hexagonalapp.domain.model.valueobject.EmailAddress;
import com.example.hexagonalapp.domain.model.valueobject.Name;
import com.example.hexagonalapp.domain.model.valueobject.UserId;
import com.example.hexagonalapp.infrastructure.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA implementation of UserRepository in the Infrastructure layer.
 * This adapter implements the output port interface using Spring Data JPA.
 * It handles data transformation between domain and persistence models.
 */
@Repository
public class JpaUserRepository implements UserRepository {
    private final SpringDataUserRepository springDataRepository;

    public JpaUserRepository(SpringDataUserRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity savedEntity = springDataRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<User> findById(Long id) {
        return springDataRepository.findById(id)
                .map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return springDataRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity(user.getName().getValue(), user.getEmail().getValue());
        if (user.getId() != null) {
            entity.setId(user.getId().getValue());
        }
        return entity;
    }

    private User toDomain(UserEntity entity) {
        Name name = new Name(entity.getName());
        EmailAddress email = new EmailAddress(entity.getEmail());
        User user = new User(name, email);
        user.setId(new UserId(entity.getId()));
        return user;
    }
}