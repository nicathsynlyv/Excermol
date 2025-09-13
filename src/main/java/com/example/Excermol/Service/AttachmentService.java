package com.example.Excermol.Service;

import com.example.Excermol.entity.Attachment;

import java.util.List;

public interface AttachmentService extends BaseService<Attachment,Long> {
    // Task-id-ə görə bütün attachment-lər
    List<Attachment> findByTask_Id(Long taskId);

    // Email-ə görə attachments
    List<Attachment> findByEmail_Id(Long emailId);
}
