package com.example.hexagonalapp.infrastructure.adapter.config;

import com.example.hexagonalapp.application.port.in.CreateUserUseCase;
import com.example.hexagonalapp.application.port.in.GetUserUseCase;
import com.example.hexagonalapp.application.port.out.EmailService;
import com.example.hexagonalapp.application.port.out.UserRepository;
import com.example.hexagonalapp.application.service.UserApplicationService;
import com.example.hexagonalapp.domain.service.UserDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring Configuration class in the Infrastructure layer.
 * This class defines bean configurations for dependency injection.
 * It wires use case interfaces to their implementations.
 */
@Configuration
public class ApplicationConfig {

    @Bean
    public UserDomainService userDomainService() {
        return new UserDomainService();
    }

    @Bean
    public CreateUserUseCase createUserUseCase(UserRepository userRepository, EmailService emailService, UserDomainService userDomainService) {
        return new UserApplicationService(userRepository, emailService, userDomainService);
    }

    @Bean
    public GetUserUseCase getUserUseCase(UserRepository userRepository, EmailService emailService, UserDomainService userDomainService) {
        return new UserApplicationService(userRepository, emailService, userDomainService);
    }
}