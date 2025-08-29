//package com.example.Excermol.controller;
//
//import com.example.Excermol.entity.Attachment;
//import com.example.Excermol.entity.Email;
//import com.example.Excermol.repository.AttachmentRepository;
//import com.example.Excermol.repository.EmailRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//
//@RestController
//@RequestMapping("/api/emails/{emailId}/attachments")
//public class EmailAttachmentController {
//    private final EmailRepository emailRepository;
//    private final AttachmentRepository attachmentRepository;
//
//    @Value("${file.upload-dir:uploads/}")
//    private String uploadDir;
//
//    public EmailAttachmentController(EmailRepository emailRepository,
//                                     AttachmentRepository attachmentRepository) {
//        this.emailRepository = emailRepository;
//        this.attachmentRepository = attachmentRepository;
//    }
//
//    // ✅ Fayl yükləmə
//    @PostMapping("/upload")
//    public Attachment uploadAttachment(@PathVariable Long emailId,
//                                       @RequestParam("file") MultipartFile file) throws IOException {
//
//        Email email = emailRepository.findById(emailId)
//                .orElseThrow(() -> new RuntimeException("Email tapılmadı!"));
//
//        // qovluq yoxdursa, yarat
//        Files.createDirectories(Paths.get(uploadDir));
//
//        // faylı qovluğa yaz
//        String filePath = uploadDir + file.getOriginalFilename();
//        Path path = Paths.get(filePath);
//        file.transferTo(path);
//
//        // Attachment obyektini yarat
//        Attachment attachment = Attachment.builder()
//                .fileName(file.getOriginalFilename())
//                .fileUrl(filePath)
//                .fileType(file.getContentType())
//                .email(email)
//                .build();
//
//        return attachmentRepository.save(attachment);
//    }
//}
