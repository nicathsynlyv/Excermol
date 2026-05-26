package com.example.Excermol.entity;

import com.example.Excermol.enums.LeadStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "campaign_leads")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CampaignLead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Əlaqədar kampaniya
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    private String leadName;       // Məs: "Anwar Hussen"
    private String leadEmail;      // "anwar380@gmail.com"

    // Şirkətlə əlaqə (Company entity-n varsa)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;    // Məs: "Mailchimp"

    // Tarixlər
    private LocalDate date;        // Kampaniyaya əlavə olunma tarixi
    private LocalDate lastActive;  // Son aktivlik

    // Proqress (0 - 100%)
    private Integer progress;


    @Enumerated(EnumType.STRING)
    private LeadStatus status;
}
