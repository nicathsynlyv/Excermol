package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.LeadStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CampaignLeadResponseDTO {
    private Long id;
    private String leadName;
    private String leadEmail;
    private String companyName;  // UI-da şirkət adı görünür
    private LocalDate date;
    private LocalDate lastActive;
    private Integer progress;
    private LeadStatus status;
}
