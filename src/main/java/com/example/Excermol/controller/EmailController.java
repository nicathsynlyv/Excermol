package com.example.Excermol.controller;

import com.example.Excermol.Service.EmailService;


import com.example.Excermol.entity.dtos.EmailRequestDto;
import com.example.Excermol.entity.dtos.EmailResponseDto;
import com.example.Excermol.enums.EmailStatus;

import io.swagger.v3.oas.annotations.Operation;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emails")
@Tag(name = "Email Controller", description = "Email əməliyyatları")
public class EmailController {

    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Operation(summary = "Bütün emailləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi")
    })
    @GetMapping
    public ResponseEntity<List<EmailResponseDto>> getAllEmails() {
        return ResponseEntity.ok(emailService.getAll());
    }

    @Operation(summary = "ID-ə görə email gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email tapıldı"),
            @ApiResponse(responseCode = "404", description = "Email tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmailResponseDto> getEmailById(@PathVariable Long id) {
        return ResponseEntity.ok(emailService.getById(id));
    }

    @Operation(summary = "Yeni email yaradır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Email uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "Yanlış request body")
    })
    @PostMapping
    public ResponseEntity<EmailResponseDto> createEmail(@Valid @RequestBody EmailRequestDto dto) {
        return ResponseEntity.status(201).body(emailService.createEmail(dto));
    }

    @Operation(summary = "Email update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email uğurla update olundu"),
            @ApiResponse(responseCode = "404", description = "Email tapılmadı"),
            @ApiResponse(responseCode = "400", description = "Yanlış request body")
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmailResponseDto> updateEmail(@PathVariable Long id,
                                                        @Valid @RequestBody EmailRequestDto dto) {
        return ResponseEntity.ok(emailService.updateEmail(id, dto));
    }

    @Operation(summary = "Email silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Email uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Email tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmail(@PathVariable Long id) {
        emailService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Status-a görə emailləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmailResponseDto>> getEmailsByStatus(@PathVariable EmailStatus status) {
        return ResponseEntity.ok(emailService.findByStatus(status));
    }

    @Operation(summary = "Oxunmamış emailləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Oxunmamış emaillər gətirildi")
    })
    @GetMapping("/unread")
    public ResponseEntity<List<EmailResponseDto>> getUnreadEmails() {
        return ResponseEntity.ok(emailService.findByReadFalse());
    }

    @Operation(summary = "Sender-ə görə emailləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi"),
            @ApiResponse(responseCode = "404", description = "User tapılmadı")
    })
    @GetMapping("/sender/{userId}")
    public ResponseEntity<List<EmailResponseDto>> getEmailsBySender(@PathVariable Long userId) {
        return ResponseEntity.ok(emailService.findBySender_Id(userId));
    }

    @Operation(summary = "Axtarış")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Axtarış nəticələri gətirildi")
    })
    @GetMapping("/search")
    public ResponseEntity<List<EmailResponseDto>> searchEmails(@RequestParam String keyword) {
        return ResponseEntity.ok(emailService.findBySubjectContainingIgnoreCase(keyword));
    }

    @Operation(summary = "Campaign-ə görə emailləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi"),
            @ApiResponse(responseCode = "404", description = "Campaign tapılmadı")
    })
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<EmailResponseDto>> getEmailsByCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(emailService.findByCampaign_Id(campaignId));
    }

    @Operation(summary = "Emaili oxunmuş kimi işarələ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email oxunmuş kimi işarələndi"),
            @ApiResponse(responseCode = "404", description = "Email tapılmadı")
    })
    @PatchMapping("/{id}/read")
    public ResponseEntity<EmailResponseDto> markAsRead(@PathVariable Long id) {
        return ResponseEntity.ok(emailService.markAsRead(id));
    }
}