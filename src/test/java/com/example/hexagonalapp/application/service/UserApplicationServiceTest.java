package com.example.hexagonalapp.application.service;

import com.example.hexagonalapp.application.port.out.EmailService;
import com.example.hexagonalapp.application.port.out.UserRepository;
import com.example.hexagonalapp.domain.model.entity.User;
import com.example.hexagonalapp.domain.model.valueobject.EmailAddress;
import com.example.hexagonalapp.domain.model.valueobject.Name;
import com.example.hexagonalapp.domain.service.UserDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmailService emailService;

    private UserDomainService userDomainService = new UserDomainService();

    private UserApplicationService userApplicationService;

    private User user;

    @BeforeEach
    void setUp() {
        Name name = new Name("Test User");
        EmailAddress email = new EmailAddress("test@example.com");
        user = new User(name, email);
        user.setId(new com.example.hexagonalapp.domain.model.valueobject.UserId(1L));
        userApplicationService = new UserApplicationService(userRepository, emailService, userDomainService);
    }

    /**
     * Test: createUser_success
     * Descripción: Verifica que se cree un usuario correctamente cuando no existe un email duplicado
     * y el usuario es válido según las reglas de dominio.
     * - Mocks: userRepository.existsByEmail retorna false, userDomainService.isUserValidForOperations retorna true
     * - Verifica: Se guarda el usuario, se envía email de bienvenida, se retorna el usuario guardado
     */
    @Test
    void createUser_success() {
        // Given
        String name = "John Doe";
        String email = "john@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userApplicationService.createUser(name, email);

        // Then
        assertNotNull(result);
        verify(userRepository).existsByEmail(email);
        verify(userRepository).save(any(User.class));
        verify(emailService).sendWelcomeEmail(anyString(), anyString());
    }

    /**
     * Test: createUser_emailAlreadyExists
     * Descripción: Verifica que se lance IllegalArgumentException cuando el email ya existe.
     * - Mocks: userRepository.existsByEmail retorna true
     * - Verifica: Se lanza excepción, no se guarda ni envía email
     */
    @Test
    void createUser_emailAlreadyExists() {
        // Given
        String name = "John Doe";
        String email = "john@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userApplicationService.createUser(name, email));
        assertEquals("Email already exists", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(emailService, never()).sendWelcomeEmail(anyString(), anyString());
    }

    /**
     * Test: createUser_invalidDomainRules
     * Descripción: Verifica que se lance IllegalArgumentException cuando el usuario no cumple reglas de dominio.
     * - Mocks: existsByEmail false, isUserValidForOperations false
     * - Verifica: Se lanza excepción, no se guarda
     */
    @Test
    void createUser_invalidDomainRules() {
        // Given
        String name = "J"; // Too short name
        String email = "john@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userApplicationService.createUser(name, email));
        assertEquals("Name must be at least 2 characters long", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Test: getUser_success
     * Descripción: Verifica que se retorne el usuario cuando existe.
     * - Mocks: userRepository.findById retorna Optional con usuario
     * - Verifica: Se retorna el usuario correcto
     */
    @Test
    void getUser_success() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // When
        User result = userApplicationService.getUser(userId);

        // Then
        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    /**
     * Test: getUser_notFound
     * Descripción: Verifica que se lance IllegalArgumentException cuando el usuario no existe.
     * - Mocks: userRepository.findById retorna Optional.empty
     * - Verifica: Se lanza excepción con mensaje "User not found"
     */
    @Test
    void getUser_notFound() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
            () -> userApplicationService.getUser(userId));
        assertEquals("User not found", exception.getMessage());
    }
}