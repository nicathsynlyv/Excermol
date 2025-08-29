package com.example.Excermol.controller;

import com.example.Excermol.Service.AttachmentService;
import com.example.Excermol.entity.Attachment;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attachments")
public class AttachmentController {


    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    // ✅ Task-a görə attachments
    @GetMapping("/task/{taskId}")
    public List<Attachment> getAttachmentsByTask(@PathVariable Long taskId) {
        return attachmentService.getAttachmentsByTask(taskId);
    }

    // ✅ Email-ə görə attachments
    @GetMapping("/email/{emailId}")
    public List<Attachment> getAttachmentsByEmail(@PathVariable Long emailId) {
        return attachmentService.getAttachmentsByEmail(emailId);
    }

    // ✅ Yeni attachment əlavə
    @PostMapping
    public Attachment addAttachment(@RequestBody Attachment attachment) {
        return attachmentService.addAttachment(attachment);
    }

    // ✅ Attachment sil
    @DeleteMapping("/{id}")
    public void deleteAttachment(@PathVariable Long id) {
        attachmentService.deleteAttachment(id);
    }
}
