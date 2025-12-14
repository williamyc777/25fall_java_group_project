package org.example.backend.domain.enums;

public enum UserType {

    Admin,
    User;

    public static UserType getInstance(String s) {
        for (UserType type : values()) {
            if (type.toString().equalsIgnoreCase(s)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid StudentType value: " + s);
    }
}
