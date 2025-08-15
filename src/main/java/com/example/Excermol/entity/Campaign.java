package com.example.Excermol.entity;

import com.example.Excermol.enums.CampaignStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.Excermol.entity.Company;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "campaigns")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kampaniya adı
    @Column(nullable = false)
    private String campaignName;

    // Şirkət adı
    @Column(nullable = false)
    private String companyName;

    // Kampaniyaya aid əlaqə email
    @Column(nullable = false)
    private String contactEmail;

    // Başlama tarixi
    private LocalDate startDate;

    // Bitmə tarixi
    private LocalDate endDate;

    // Proqres (faiz olaraq)
    private Double progress;

    // Status (Sent, Opened, Clicked, Replied və s.)
    @Enumerated(EnumType.STRING)
    private CampaignStatus status;

    // Son aktivlik tarixi
    private LocalDateTime lastActive;

    // Email statistikaları
    private Integer sentEmails;
    private Integer openedEmails;
    private Integer repliedEmails;
    private Integer bouncedEmails;

    // İstifadəçi ilə əlaqə (hansı user yaratdı)
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
//company ile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    private List<Email> emails;

    @ManyToMany
    @JoinTable(
            name = "campaign_person",
            joinColumns = @JoinColumn(name = "campaign_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id")
    )
    private List<Person> persons;
}
