package com.example.Excermol.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
//hidden swagger de gorunmesin deye qoymusam
@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Not Found
    @ExceptionHandler({
            TaskNotFoundException.class,
            EmailNotFoundException.class,
            OrganizationNotFoundException.class,
            CampaignNotFoundException.class,
            LeadNotFoundException.class,
            ResourceNotFoundException.class,
            UserNotFoundException.class,
            CompanyNotFoundException.class,
            PersonNotFoundException.class,
            ActivityNotFoundException.class,
            NoteNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // 409 - Conflict
    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            DomainAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // 400 - Validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Validation xətası");
        return buildResponse(HttpStatus.BAD_REQUEST, message);
    }

    // 500 - Generic
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // Köməkçi metod
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);
        return new ResponseEntity<>(response, status);
    }
}