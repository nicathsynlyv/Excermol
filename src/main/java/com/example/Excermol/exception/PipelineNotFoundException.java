package com.example.Excermol.exception;

public class PipelineNotFoundException extends RuntimeException {
    public PipelineNotFoundException(String message) {
        super(message);
    }
}
