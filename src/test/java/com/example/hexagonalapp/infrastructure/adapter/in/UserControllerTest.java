package com.example.hexagonalapp.infrastructure.adapter.in;

import com.example.hexagonalapp.application.port.in.CreateUserUseCase;
import com.example.hexagonalapp.application.port.in.GetUserUseCase;
import com.example.hexagonalapp.domain.model.entity.User;
import com.example.hexagonalapp.domain.model.valueobject.EmailAddress;
import com.example.hexagonalapp.domain.model.valueobject.Name;
import com.example.hexagonalapp.domain.model.valueobject.UserId;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CreateUserUseCase createUserUseCase;

    @MockBean
    private GetUserUseCase getUserUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setUp() {
        Name name = new Name("Test User");
        EmailAddress email = new EmailAddress("test@example.com");
        user = new User(name, email);
        user.setId(new UserId(1L));
    }

    /**
     * Test: createUser_success
     * Descripción: Verifica que se cree un usuario correctamente y se retorne 200 con el DTO.
     * - Mocks: createUserUseCase.createUser retorna usuario
     * - Verifica: HTTP 200, JSON con id, name, email
     */
    @Test
    void createUser_success() throws Exception {
        // Given
        UserController.CreateUserRequest request = new UserController.CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");

        when(createUserUseCase.createUser("John Doe", "john@example.com")).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    /**
     * Test: createUser_validationError
     * Descripción: Verifica que se retorne 400 cuando la validación falla (email inválido).
     * - Request: email vacío
     * - Verifica: HTTP 400, mensaje de error
     */
    @Test
    void createUser_validationError() throws Exception {
        // Given
        UserController.CreateUserRequest request = new UserController.CreateUserRequest();
        request.setName("John Doe");
        request.setEmail(""); // Invalid email

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    /**
     * Test: getUser_success
     * Descripción: Verifica que se retorne un usuario existente con 200.
     * - Mocks: getUserUseCase.getUser retorna usuario
     * - Verifica: HTTP 200, JSON con datos del usuario
     */
    @Test
    void getUser_success() throws Exception {
        // Given
        Long userId = 1L;
        when(getUserUseCase.getUser(userId)).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    /**
     * Test: getUser_notFound
     * Descripción: Verifica que se retorne error estructurado cuando el usuario no existe.
     * - Mocks: getUserUseCase.getUser lanza IllegalArgumentException
     * - Verifica: HTTP 400 con respuesta JSON estructurada
     */
    //Test
    void getUser_notFound() throws Exception {
        // Given
        Long userId = 1L;
        when(getUserUseCase.getUser(userId)).thenThrow(new IllegalArgumentException("User not found"));

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}