package com.example.hexagonalapp.application.service;

import com.example.hexagonalapp.application.port.in.CreateUserUseCase;
import com.example.hexagonalapp.application.port.in.GetUserUseCase;
import com.example.hexagonalapp.application.port.out.EmailService;
import com.example.hexagonalapp.application.port.out.UserRepository;
import com.example.hexagonalapp.domain.model.domainevent.UserCreatedEvent;
import com.example.hexagonalapp.domain.model.entity.User;
import com.example.hexagonalapp.domain.service.UserDomainService;
import com.example.hexagonalapp.domain.model.valueobject.EmailAddress;
import com.example.hexagonalapp.domain.model.valueobject.Name;

/**
 * ApplicationService that orchestrates User-related use cases in the Application layer.
 * This service implements the input port interfaces, providing concrete implementations of use cases.
 * It coordinates between domain objects and output ports, ensuring use case orchestration.
 */
public class UserApplicationService implements CreateUserUseCase, GetUserUseCase {
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserDomainService userDomainService;

    public UserApplicationService(UserRepository userRepository, EmailService emailService, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.userDomainService = userDomainService;
    }

    public User createUser(String nameIn, String emailIn) {
        // Validate email uniqueness
        if (userRepository.existsByEmail(emailIn)) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Create domain object
        EmailAddress email = new EmailAddress(emailIn);
        Name name = new Name(nameIn);
        User user = new User(name, email);

        // Validate user using domain service
        if (!userDomainService.isUserValidForOperations(user)) {
            throw new IllegalArgumentException("User does not meet domain validation rules");
        }

        // Save user
        User savedUser = userRepository.save(user);

        // Send welcome email (fire and forget)
        emailService.sendWelcomeEmail(savedUser.getEmail().getValue(), savedUser.getName().getValue());

        // Domain event could be published here
        UserCreatedEvent event = new UserCreatedEvent(savedUser);
        // In a real app, publish to event bus

        return savedUser;
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}