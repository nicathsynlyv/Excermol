package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.LeadStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CampaignLeadRequestDTO {
    private String leadName;
    private String leadEmail;
    private Long companyId;
    private Long campaignId;
    private LocalDate date;
    private Integer progress;
    private LeadStatus status;
}
