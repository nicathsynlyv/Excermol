package com.example.Excermol.exception;

public class OwnerRoleChangeNotAllowedException extends RuntimeException {
    public OwnerRoleChangeNotAllowedException(String message) {
        super(message);
    }
}
