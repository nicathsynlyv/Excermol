package com.example.Excermol.exception;

import com.example.Excermol.security.jwt.RefreshTokenException;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Hidden
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // =========================
    // 404 - Not Found
    // =========================
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
            WorkspaceMemberNotFoundException.class,
            NotificationSettingNotFoundException.class,
            CompanyAttributeNotFoundException.class,
            IntegrationNotFoundException.class,
            TagNotFoundException.class,
            CommentNotFoundException.class,
            AttachmentNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // =========================
    // 409 - Conflict
    // =========================
    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            DomainAlreadyExistsException.class,
            UserAlreadyMemberException.class,
            NotificationSettingAlreadyExistsException.class,
            IntegrationAlreadyExistsException.class,
            TagAlreadyExistsException.class
    })
    public ResponseEntity<Map<String, Object>> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // =========================
    // 403 - Forbidden
    // =========================
    @ExceptionHandler({
            OwnerCannotLeaveWorkspaceException.class,
            OwnerRoleChangeNotAllowedException.class,
            OwnerCannotBeRemovedException.class,
            SystemAttributeCannotBeModifiedException.class
    })
    public ResponseEntity<Map<String, Object>> handleForbidden(RuntimeException ex) {
        return buildResponse(HttpStatus.FORBIDDEN, ex.getMessage());
    }


    // =========================
// 401 - Authentication Errors (Login zamanı) securiy ucun
// =========================
    @ExceptionHandler({
            BadCredentialsException.class,
            AuthenticationException.class,
            RefreshTokenException.class  // yeni əlavə olundu

    })
    public ResponseEntity<Map<String, Object>> handleAuthenticationErrors(Exception ex) {
        log.warn("Authentication failed: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    // =========================
// 403 - Access Denied (Rol yetərsizdir)
// =========================
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(AccessDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage());
        return buildResponse(HttpStatus.FORBIDDEN, "Bu əməliyyat üçün səlahiyyətiniz yoxdur");
    }

    // =========================
    // 400 - Validation Errors
    // =========================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        response.put("messages", errors);

        return ResponseEntity.badRequest().body(response);
    }

    // =========================
    // 400 - IllegalArgumentException
    // =========================
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // =========================
    // 500 - Generic Exception
    // =========================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {
        log.error("Unexpected error occurred", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Daxili server xətası baş verdi");
    }

    // =========================
    // KÖMƏKÇI METOD
    // =========================
    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status, String message) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        return ResponseEntity.status(status).body(response);
    }
}