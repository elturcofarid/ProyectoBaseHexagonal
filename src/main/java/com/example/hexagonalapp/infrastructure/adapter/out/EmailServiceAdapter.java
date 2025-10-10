package com.example.hexagonalapp.infrastructure.adapter.out;

import com.example.hexagonalapp.application.port.out.EmailService;
import org.springframework.stereotype.Service;

/**
 * Adapter implementation of EmailService in the Infrastructure layer.
 * This class implements the output port interface for email functionality.
 * In a real application, this would integrate with an actual email service provider.
 */
@Service
public class EmailServiceAdapter implements EmailService {
    @Override
    public void sendWelcomeEmail(String email, String name) {
        // Simulate sending email - in real implementation, use JavaMailSender or external API
        System.out.println("Sending welcome email to " + email + " for user " + name);
        // Actual implementation would call email service
    }
}