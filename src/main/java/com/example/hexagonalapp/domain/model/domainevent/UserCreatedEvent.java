package com.example.hexagonalapp.domain.model.domainevent;

import com.example.hexagonalapp.domain.model.entity.User;

/**
 * DomainEvent representing the creation of a User in the Domain layer.
 * This event can be used to trigger side effects or notifications when a user is created.
 * It follows the Domain Event pattern for decoupling domain logic.
 */
public class UserCreatedEvent {
    private final User user;

    public UserCreatedEvent(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "UserCreatedEvent{" +
                "user=" + user +
                '}';
    }
}