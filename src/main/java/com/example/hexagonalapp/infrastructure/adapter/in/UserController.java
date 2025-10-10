package com.example.hexagonalapp.infrastructure.adapter.in;

import com.example.hexagonalapp.application.dto.CreateUserCommand;
import com.example.hexagonalapp.application.dto.GetUserQuery;
import com.example.hexagonalapp.application.port.in.CreateUserUseCase;
import com.example.hexagonalapp.application.port.in.GetUserUseCase;
import com.example.hexagonalapp.domain.model.entity.User;
import com.example.hexagonalapp.domain.model.valueobject.UserId;
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
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(request.getName(), request.getEmail());
        User user = createUserUseCase.createUser(command);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable Long id) {
        GetUserQuery query = new GetUserQuery(new UserId(id));
        User user = getUserUseCase.getUser(query);
        return ResponseEntity.ok(user);
    }

    // DTO for request body
    public static class CreateUserRequest {
        private String name;
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