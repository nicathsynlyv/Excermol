package com.example.Excermol.exception;

public class UserAlreadyMemberException extends RuntimeException {
    public UserAlreadyMemberException(String message) {
        super(message);
    }
}
