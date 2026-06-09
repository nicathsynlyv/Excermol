package com.example.Excermol.mapper;

import com.example.Excermol.entity.Attachment;
import com.example.Excermol.entity.dtos.AttachmentResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class AttachmentMapper {

    // Entity → ResponseDTO
    public AttachmentResponseDTO toResponseDTO(Attachment attachment) {
        AttachmentResponseDTO dto = new AttachmentResponseDTO();
        dto.setId(attachment.getId());
        dto.setFileName(attachment.getFileName());
        dto.setFileUrl(attachment.getFileUrl());
        dto.setFileType(attachment.getFileType());

        // task
        if (attachment.getTask() != null) {
            dto.setTaskId(attachment.getTask().getId());
            dto.setTaskTitle(attachment.getTask().getTitle());
        }

        // uploadedBy
        if (attachment.getUploadedBy() != null) {
            dto.setUploadedById(attachment.getUploadedBy().getId());
            dto.setUploadedByName(attachment.getUploadedBy().getFullName());
        }

        // email
        if (attachment.getEmail() != null) {
            dto.setEmailId(attachment.getEmail().getId());
            dto.setEmailSubject(attachment.getEmail().getSubject());
        }

        return dto;
    }
}
