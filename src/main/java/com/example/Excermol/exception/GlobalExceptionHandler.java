package com.example.Excermol.exception;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
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
            NoteNotFoundException.class,
            PipelineNotFoundException.class,
            FormNotFoundException.class,
            FormFieldNotFoundException.class,
            FormResponseNotFoundException.class,
            FormRoutingNotFoundException.class,
            WorkspaceNotFoundException.class,
            MemberNotFoundException.class,
            OwnerCannotLeaveWorkspaceException.class,
            WorkspaceMemberNotFoundException.class,
            UserAlreadyMemberException.class,
            OwnerRoleChangeNotAllowedException.class,
            OwnerCannotBeRemovedException.class,
            NotificationSettingNotFoundException.class,
            NotificationSettingAlreadyExistsException.class,
            CompanyAttributeNotFoundException.class,
            SystemAttributeCannotBeModifiedException.class,
            IntegrationNotFoundException.class,
            IntegrationAlreadyExistsException.class,
            TagAlreadyExistsException.class,
            TagNotFoundException.class,
            CommentNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildResponse(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
    }

    // 409 - Conflict
    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            DomainAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
    }

    // 400 - Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("messages", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // 400 - IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                ex.getMessage()
        );
    }

    // 500 - Generic Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(
            Exception ex) {

        ex.printStackTrace();

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Daxili server xətası baş verdi"
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return ResponseEntity
                .status(status)
                .body(response);
    }
}