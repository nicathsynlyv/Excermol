package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.CampaignStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class CampaignResponseDto {
    private Long id;
    private String name;
    private CampaignStatus status;
    private LocalDateTime createdAt;

    // Hesablanmış statistikalar
    private Integer sequenceStarted;
    private Double openRate;
    private Double replyRate;
    private Double bounceRate;


    // User ← new changes
    private Long userId;
}
