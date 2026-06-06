package com.example.Excermol.exception;

public class OwnerCannotBeRemovedException extends RuntimeException {
    public OwnerCannotBeRemovedException(String message) {
        super(message);
    }
}
