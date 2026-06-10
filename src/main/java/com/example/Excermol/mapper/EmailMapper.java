package com.example.Excermol.mapper;

import com.example.Excermol.entity.Attachment;
import com.example.Excermol.entity.Email;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.dtos.EmailRequestDto;
import com.example.Excermol.entity.dtos.EmailResponseDto;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EmailMapper {
    public Email toEntity(EmailRequestDto dto) {
        Email email = new Email();
        email.setSubject(dto.getSubject());
        email.setBody(dto.getBody());
        email.setStatus(dto.getStatus());
        email.setSentAt(dto.getSentAt());
        email.setLabels(dto.getLabels());

        return email;
    }

    public EmailResponseDto toResponse(Email email) {
        EmailResponseDto dto = new EmailResponseDto();
        dto.setId(email.getId());
        dto.setSubject(email.getSubject());
        dto.setBody(email.getBody());
        dto.setStatus(email.getStatus());
        dto.setRead(email.isRead());
        dto.setCreatedAt(email.getCreatedAt());
        dto.setSentAt(email.getSentAt());
        dto.setUpdatedAt(email.getUpdatedAt());
        dto.setLabels(email.getLabels());

        // Sender
        if (email.getSender() != null) {
            dto.setSenderName(email.getSender().getFullName());
        }

        // Company
        if (email.getCompany() != null) {
            dto.setCompanyName(email.getCompany().getCompanyName());
        }

        // Campaign
        if (email.getCampaign() != null) {
            dto.setCampaignName(email.getCampaign().getName());
        }

        // Recipients
        if (email.getRecipients() != null) {
            dto.setRecipientNames(
                    email.getRecipients()
                            .stream()
                            .map(Person::getFullName)
                            .collect(Collectors.toSet())
            );
        }

        // Attachments
        if (email.getAttachments() != null) {
            dto.setAttachmentNames(
                    email.getAttachments()
                            .stream()
                            .map(Attachment::getFileName)
                            .collect(Collectors.toList())
            );
//            dto.setAttachmentsCount(email.getAttachments().size()); // ← əlavə et
//        }else {
//            dto.setAttachmentsCount(0);
        }

        return dto;
    }

    public List<EmailResponseDto> toResponseList(List<Email> emails) {
        if (emails == null) return Collections.emptyList();
        return emails.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
