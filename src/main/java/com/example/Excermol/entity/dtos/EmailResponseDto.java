package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.EmailStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EmailResponseDto {

    private Long id;

    private String subject;

    private String body;

    private EmailStatus status;

    private boolean read;

    private LocalDateTime createdAt;
    private LocalDateTime sentAt;
    private LocalDateTime updatedAt;

    private List<String> labels;

    // RELATION məlumatları
    private String senderName;      // User-in adı

    private String companyName;     // Company-nin adı

    private String campaignName;    // Campaign-in adı

    private Set<String> recipientNames; // Person-ların adları

    // Attachments
    private List<String> attachmentNames;
}
