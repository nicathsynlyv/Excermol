package com.example.Excermol.exception;

public class DomainAlreadyExistsException extends RuntimeException {
    public DomainAlreadyExistsException(String message) {
        super(message);
    }
}
