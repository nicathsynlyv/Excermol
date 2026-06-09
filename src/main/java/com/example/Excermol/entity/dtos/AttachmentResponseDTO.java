package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttachmentResponseDTO {

    private Long id;
    private String fileName;
    private String fileUrl;
    private String fileType;

    // task
    private Long taskId;
    private String taskTitle;

    // uploadedBy
    private Long uploadedById;
    private String uploadedByName;

    // email
    private Long emailId;
    private String emailSubject;
}

