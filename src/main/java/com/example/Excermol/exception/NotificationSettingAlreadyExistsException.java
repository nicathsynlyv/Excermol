package com.example.Excermol.exception;

public class NotificationSettingAlreadyExistsException extends RuntimeException {
    public NotificationSettingAlreadyExistsException(String message) {
        super(message);
    }
}
