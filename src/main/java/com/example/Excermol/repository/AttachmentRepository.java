package com.example.Excermol.repository;

import com.example.Excermol.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository  extends JpaRepository<Attachment, Long> {
    // Task-id-ə görə bütün attachment-lər
    List<Attachment> findByTask_Id(Long taskId);

    // Email-ə görə attachments
    List<Attachment> findByEmail_Id(Long emailId);
}
