package com.example.Excermol.exception;

public class OwnerCannotLeaveWorkspaceException extends RuntimeException {
    public OwnerCannotLeaveWorkspaceException(String message) {
        super(message);
    }
}
