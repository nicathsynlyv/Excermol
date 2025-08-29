package com.example.Excermol.entity;

import com.example.Excermol.enums.CampaignStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Kampaniyanın adı
    @Column(nullable = false)
    private String name;

    // Statistikalar
    private Integer sequenceStarted;   // 435
    private Double openRate;           // 32.1%
    private Double replyRate;          // 27.9%
    private Double bounceRate;         // 18.6%

    private LocalDate createdAt;


    @Enumerated(EnumType.STRING)
    private CampaignStatus status;  // DRAFT, ACTIVE, COMPLETED, PAUSED

    // 1 kampaniyada çox lead ola bilər
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignLead> leads;


   //email ile
   @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL, orphanRemoval = true)
   private List<Email> emails;

    // İstifadəçi ilə əlaqə (hansı user yaratdı)
//    @ManyToOne
//    @JoinColumn(name = "created_by")
//    private User createdBy;


//    @ManyToMany
//    @JoinTable(
//            name = "campaign_person",
//            joinColumns = @JoinColumn(name = "campaign_id"),
//            inverseJoinColumns = @JoinColumn(name = "person_id")
//    )
//    private List<Person> persons;
}
