//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.AttachmentService;
//import com.example.Excermol.entity.dtos.AttachmentResponseDTO;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/attachments")
//@Tag(name = "Attachment Controller", description = "Attachment management APIs")
//public class AttachmentController {
//
//    private final AttachmentService attachmentService;
//
//    public AttachmentController(AttachmentService attachmentService) {
//        this.attachmentService = attachmentService;
//    }
//
//    @Operation(summary = "Email-ə fayl əlavə et")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Attachment yaradıldı"),
//            @ApiResponse(responseCode = "404", description = "Email və ya User tapılmadı")
//    })
//    @PostMapping("/emails/{emailId}")
//    public ResponseEntity<AttachmentResponseDTO> uploadToEmail(
//            @PathVariable Long emailId,
//            @RequestParam Long userId,
//            @RequestParam("file") MultipartFile file) throws IOException {
//
//        AttachmentResponseDTO response =
//                attachmentService.uploadToEmail(emailId, userId, file);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    @Operation(summary = "Task-a fayl əlavə et")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Attachment yaradıldı"),
//            @ApiResponse(responseCode = "404", description = "Task və ya User tapılmadı")
//    })
//    @PostMapping("/tasks/{taskId}")
//    public ResponseEntity<AttachmentResponseDTO> uploadToTask(
//            @PathVariable Long taskId,
//            @RequestParam Long userId,
//            @RequestParam("file") MultipartFile file) throws IOException {
//
//        AttachmentResponseDTO response =
//                attachmentService.uploadToTask(taskId, userId, file);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(response);
//    }
//
//    @Operation(summary = "Email attachmentlərini gətir")
//    @GetMapping("/emails/{emailId}")
//    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByEmailId(
//            @PathVariable Long emailId) {
//
//        return ResponseEntity.ok(
//                attachmentService.getAttachmentsByEmailId(emailId)
//        );
//    }
//
//    @Operation(summary = "Task attachmentlərini gətir")
//    @GetMapping("/tasks/{taskId}")
//    public ResponseEntity<List<AttachmentResponseDTO>> getAttachmentsByTaskId(
//            @PathVariable Long taskId) {
//
//        return ResponseEntity.ok(
//                attachmentService.getAttachmentsByTaskId(taskId)
//        );
//    }
//
//    @Operation(summary = "ID-yə görə attachment gətir")
//    @ApiResponses({
//            @ApiResponse(responseCode = "200", description = "Attachment tapıldı"),
//            @ApiResponse(responseCode = "404", description = "Attachment tapılmadı")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<AttachmentResponseDTO> getAttachmentById(
//            @PathVariable Long id) {
//
//        return ResponseEntity.ok(
//                attachmentService.getAttachmentById(id)
//        );
//    }
//
//    @Operation(summary = "Attachment sil")
//    @ApiResponses({
//            @ApiResponse(responseCode = "204", description = "Attachment silindi"),
//            @ApiResponse(responseCode = "404", description = "Attachment tapılmadı")
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteAttachment(
//            @PathVariable Long id) throws IOException {
//
//        attachmentService.deleteAttachment(id);
//
//        return ResponseEntity.noContent().build();
//    }
//}