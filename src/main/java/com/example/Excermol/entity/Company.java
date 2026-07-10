package com.example.Excermol.entity;

import com.example.Excermol.enums.CompanyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Company {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Lead adi sadəcə string
    @Size(max = 100)
    @Column(length = 100)
    private String leadSource;


    // Status enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    //private CompanyStatus status;
    private CompanyStatus status = CompanyStatus.INTERESTED; // ✅


    // Şəhər
    @Size(max = 100)
    @Column(length = 100)
    private String city;

    // Lead value
    @DecimalMin("0.0")
    @Column(name = "lead_value", precision = 15, scale = 2)
    private BigDecimal leadValue;

    // Connection (məs: Zoho CRM, Pipedrive və s.)
    private String connection;

    @NotBlank
    @Size(min =2,max = 150)
    @Column(nullable = false, length = 150)
    private String companyName;

    // Domain
    @Size(max = 255)
    @Column(length = 255, unique = true)
    private String domain;


    // 🔗 Task ilə əlaqə
    @OneToMany(mappedBy = "company")
    private List<Task> tasks = new ArrayList<>();


    // Bir şirkətin çoxlu lead-i ola bilər compaign lead ile elaqe
    @OneToMany(mappedBy = "company")
    private List<CampaignLead> leads = new ArrayList<>();


    // Bir şirkətə bir neçə şəxs aid ola bilər (One-to-Many)
    // ✅ Düzgün
    @OneToMany(mappedBy = "company")
    private Set<Person> employees = new HashSet<>();  // şirkətdə işləyən bütün şəxslər

    // ✅ Person ilə əlaqə
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Person owner;  // şirkətin sahibi


    @OneToMany(mappedBy = "company")
    private List<Pipeline> pipelines = new ArrayList<>();  // ✅

    @OneToMany(mappedBy = "company")
    private List<Email> emails = new ArrayList<>();  // ✅

    // ✅ Şirkətin contact email-i üçün sadə string
    @jakarta.validation.constraints.Email
    @Size(max = 150)
    @Column(length = 150)
    private String emailAddress; // "anwarhussen@gmail.com"


    //gelecekde user ile relation duzeltmek ucun
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}



