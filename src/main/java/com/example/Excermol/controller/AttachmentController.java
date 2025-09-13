package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.AttachmentServiceImpl;
import com.example.Excermol.entity.Attachment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
@Tag(name = "Attachments API", description = "Task və Email attachments idarəsi")
public class AttachmentController {
    private final AttachmentServiceImpl attachmentServiceImpl;

    public AttachmentController(AttachmentServiceImpl attachmentServiceImpl) {
        this.attachmentServiceImpl = attachmentServiceImpl;
    }

    @Operation(summary = "Bütün attachments-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments-lər tapıldı",
                    content = @Content(schema = @Schema(implementation = Attachment.class))),
            @ApiResponse(responseCode = "404", description = "Attachments tapılmadı")
    })
    @GetMapping
    public ResponseEntity<List<Attachment>> getAllAttachments() {
        return ResponseEntity.ok(attachmentServiceImpl.getAll());
    }

    @Operation(summary = "ID-ə görə attachment gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachment tapıldı",
                    content = @Content(schema = @Schema(implementation = Attachment.class))),
            @ApiResponse(responseCode = "404", description = "Attachment tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Attachment> getAttachmentById(@PathVariable Long id) {
        return attachmentServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Task-a görə attachments-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments-lər tapıldı",
                    content = @Content(schema = @Schema(implementation = Attachment.class))),
            @ApiResponse(responseCode = "404", description = "Attachments tapılmadı")
    })
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(attachmentServiceImpl.findByTask_Id(taskId));
    }

    @Operation(summary = "Email-ə görə attachments-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attachments-lər tapıldı",
                    content = @Content(schema = @Schema(implementation = Attachment.class))),
            @ApiResponse(responseCode = "404", description = "Attachments tapılmadı")
    })
    @GetMapping("/email/{emailId}")
    public ResponseEntity<List<Attachment>> getAttachmentsByEmail(@PathVariable Long emailId) {
        return ResponseEntity.ok(attachmentServiceImpl.findByEmail_Id(emailId));
    }

    @Operation(summary = "Yeni attachment əlavə edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Attachment yaradıldı",
                    content = @Content(schema = @Schema(implementation = Attachment.class))),
            @ApiResponse(responseCode = "400", description = "Yaranma zamanı səhv baş verdi")
    })
    @PostMapping
    public ResponseEntity<Attachment> addAttachment(@RequestBody Attachment attachment) {
        return ResponseEntity.status(201).body(attachmentServiceImpl.save(attachment));
    }

    @Operation(summary = "Attachment silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attachment silindi"),
            @ApiResponse(responseCode = "404", description = "Attachment tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long id) {
        if (attachmentServiceImpl.getById(id).isPresent()) {
            attachmentServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}