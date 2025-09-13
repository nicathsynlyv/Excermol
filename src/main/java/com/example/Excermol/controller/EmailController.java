package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.EmailServiceImpl;
import com.example.Excermol.entity.Email;
import com.example.Excermol.enums.EmailStatus;
import com.example.Excermol.exception.EmailNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
public class EmailController {

    private final EmailServiceImpl emailServiceImpl;

    public EmailController(EmailServiceImpl emailServiceImpl) {
        this.emailServiceImpl = emailServiceImpl;
    }

    @Operation(summary = "Bütün emailləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi",
                    content = @Content(schema = @Schema(implementation = Email.class)))
    })
    @GetMapping
    public ResponseEntity<List<Email>> getAllEmails() {
        return ResponseEntity.ok(emailServiceImpl.getAll());
    }

    @Operation(summary = "ID-ə görə email gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email tapıldı",
                    content = @Content(schema = @Schema(implementation = Email.class))),
            @ApiResponse(responseCode = "404", description = "Email tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Email> getEmailById(@PathVariable Long id) {
        return emailServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni email yaradır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = Email.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Email> createEmail(@RequestBody Email email) {
        return ResponseEntity.status(201).body(emailServiceImpl.save(email));
    }

    @Operation(summary = "Email update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email uğurla update olundu",
                    content = @Content(schema = @Schema(implementation = Email.class))),
            @ApiResponse(responseCode = "404", description = "Email tapılmadı", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Email> updateEmail(@PathVariable Long id, @RequestBody Email updatedEmail) {
        Email updated = emailServiceImpl.updateEmail(id, updatedEmail);
        return ResponseEntity.ok(updated);
    }


    @Operation(summary = "Email silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Email tapılmadı", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        Email email = emailServiceImpl.getById(id)
                .orElseThrow(() -> new EmailNotFoundException("Email ID = " + id + " tapılmadı!"));
        emailServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    // Status-a görə email-ləri gətir
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Email>> getEmailsByStatus(@PathVariable EmailStatus status) {
        return ResponseEntity.ok(emailServiceImpl.findByStatus(status));
    }

    // Oxunma statusuna görə
    @GetMapping("/isRead/{isRead}")
    public ResponseEntity<List<Email>> getEmailsByReadStatus(@PathVariable boolean isRead) {
        return ResponseEntity.ok(emailServiceImpl.findByIsRead(isRead));
    }

    // Göndəriciyə görə
    @GetMapping("/sender/{sender}")
    public ResponseEntity<List<Email>> getEmailsBySender(@PathVariable String sender) {
        return ResponseEntity.ok(emailServiceImpl.findBySender(sender));
    }

    // Alıcıya görə
    @GetMapping("/recipient/{recipient}")
    public ResponseEntity<List<Email>> getEmailsByRecipient(@PathVariable String recipient) {
        return ResponseEntity.ok(emailServiceImpl.findByRecipientsContaining(recipient));
    }
}
