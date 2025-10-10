package com.example.hexagonalapp.domain.model.domainexception;

/**
 * DomainException representing invalid email errors in the Domain layer.
 * This exception is thrown when email validation fails in domain logic.
 * It encapsulates domain-specific error handling.
 */
public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException(String message) {
        super(message);
    }

    public InvalidEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}