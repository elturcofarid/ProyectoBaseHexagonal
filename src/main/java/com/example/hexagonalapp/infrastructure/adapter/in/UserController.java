package com.example.hexagonalapp.infrastructure.adapter.in;

import com.example.hexagonalapp.application.port.in.CreateUserUseCase;
import com.example.hexagonalapp.application.port.in.GetUserUseCase;
import com.example.hexagonalapp.domain.model.entity.User;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for User operations in the Infrastructure layer.
 * This adapter depends on input port interfaces from the Application layer.
 * It handles HTTP requests and responses, delegating to use case interfaces.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;

    public UserController(CreateUserUseCase createUserUseCase, GetUserUseCase getUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        User user = createUserUseCase.createUser(request.getName(), request.getEmail());
        UserResponse response = new UserResponse(
            user.getId().getValue(),
            user.getName().getValue(),
            user.getEmail().getValue()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        User user = getUserUseCase.getUser(id);
        UserResponse response = new UserResponse(
            user.getId().getValue(),
            user.getName().getValue(),
            user.getEmail().getValue()
        );
        return ResponseEntity.ok(response);
    }

    // DTO for request body
    public static class CreateUserRequest {
        @NotBlank(message = "Name is required")
        private String name;

        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        private String email;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}