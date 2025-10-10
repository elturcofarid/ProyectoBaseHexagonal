package com.example.hexagonalapp.domain.model.valueobject;

import java.util.Objects;

/**
 * ValueObject representing a Name in the Domain layer.
 * This encapsulates name validation and ensures immutability.
 */
public class Name {
    private final String value;

    public Name(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (value.trim().length() < 2) {
            throw new IllegalArgumentException("Name must be at least 2 characters long");
        }
        this.value = value.trim();
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name = (Name) o;
        return Objects.equals(value, name.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}