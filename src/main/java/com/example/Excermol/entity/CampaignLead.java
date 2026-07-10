package com.example.Excermol.entity;

import com.example.Excermol.enums.LeadStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Email;
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

    @NotBlank
    @Size(min = 2,max = 100)
    @Column(nullable = false, length = 100)
    private String leadName;       // Məs: "Anwar Hussen"

    @NotBlank
    @Email
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String leadEmail;      // "anwar380@gmail.com"

    // Şirkətlə əlaqə (Company entity-n varsa)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;    // Məs: "Mailchimp"

    // Tarixlər
    private LocalDate date;        // Kampaniyaya əlavə olunma tarixi
    private LocalDate lastActive;  // Son aktivlik

    // Proqress (0 - 100%)
    @Min(0) @Max(100)
    private Integer progress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LeadStatus status;
}
