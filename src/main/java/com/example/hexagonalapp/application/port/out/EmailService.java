package com.example.hexagonalapp.application.port.out;

/**
 * Port interface for email services in the Application layer.
 * This output port defines the contract for sending emails.
 * It allows the application layer to remain independent of specific email providers.
 */
public interface EmailService {
    void sendWelcomeEmail(String email, String name);
}