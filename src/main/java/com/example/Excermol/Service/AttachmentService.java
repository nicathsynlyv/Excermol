package com.example.Excermol.Service;

import com.example.Excermol.entity.Attachment;
import com.example.Excermol.repository.AttachmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class AttachmentService {
    private final AttachmentRepository attachmentRepository;

    public AttachmentService(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    // ✅ Task-a görə attachments
    public List<Attachment> getAttachmentsByTask(Long taskId) {
        return attachmentRepository.findByTask_Id(taskId);
    }

    // ✅ Email-ə görə attachments
    public List<Attachment> getAttachmentsByEmail(Long emailId) {
        return attachmentRepository.findByEmail_Id(emailId);
    }

    // ✅ Yeni attachment əlavə et
    public Attachment addAttachment(Attachment attachment) {
        return attachmentRepository.save(attachment);
    }

    // ✅ Attachment sil
    public void deleteAttachment(Long id) {
        attachmentRepository.deleteById(id);
    }
}
