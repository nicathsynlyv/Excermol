package com.example.Excermol.exception;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException(String message) {
        super(message);
    }
}
