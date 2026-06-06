package com.example.Excermol.exception;

public class WorkspaceMemberNotFoundException extends RuntimeException {
    public WorkspaceMemberNotFoundException(String message) {
        super(message);
    }
}
