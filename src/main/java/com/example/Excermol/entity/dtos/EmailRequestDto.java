package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.EmailStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class EmailRequestDto {
    private String subject;

    private String body;

    private EmailStatus status;



    private LocalDateTime sentAt;

    private List<String> labels;

    // RELATION IDS
    private Long senderId;

    private Long companyId;

    private Long campaignId;

    private Set<Long> recipientIds; // Person id-ləri
}
