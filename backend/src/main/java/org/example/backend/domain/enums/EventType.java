package org.example.backend.domain.enums;

public enum EventType {

    Lecture,

    Competition;

    public static EventType getInstance(String s) {
        for (EventType type : values()) {
            if (type.toString().equalsIgnoreCase(s)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid StudentType value: " + s);
    }
}
