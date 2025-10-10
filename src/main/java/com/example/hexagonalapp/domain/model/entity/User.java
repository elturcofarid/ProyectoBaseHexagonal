package com.example.hexagonalapp.domain.model.entity;

import com.example.hexagonalapp.domain.model.valueobject.EmailAddress;
import com.example.hexagonalapp.domain.model.valueobject.Name;
import com.example.hexagonalapp.domain.model.valueobject.UserId;

/**
 * Entity representing a User in the Domain layer.
 * This class encapsulates the core business logic and state of a user.
 * It is framework-independent and focuses on domain behavior.
 * Uses value objects for all properties to ensure immutability and validation.
 */
public class User {
    private UserId id;
    private Name name;
    private EmailAddress email;

    // Default constructor for frameworks
    protected User() {}

    public User(Name name, EmailAddress email) {
        this.name = name;
        this.email = email;
    }

    public UserId getId() {
        return id;
    }

    public void setId(UserId id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public EmailAddress getEmail() {
        return email;
    }

    public void setEmail(EmailAddress email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                '}';
    }
}