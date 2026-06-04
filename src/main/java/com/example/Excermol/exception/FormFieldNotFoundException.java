package com.example.Excermol.exception;

public class FormFieldNotFoundException extends RuntimeException {
    public FormFieldNotFoundException(String message) {
        super(message);
    }
}
