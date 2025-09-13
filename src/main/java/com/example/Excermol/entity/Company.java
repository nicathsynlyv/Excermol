package com.example.Excermol.entity;

import com.example.Excermol.enums.CompanyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String owner;

    // Lead adi sadəcə string
    @Column(nullable = false)
    private String lead;
    //email ile elaqe
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id")
    private Email emailAddress;


    // Status enum
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CompanyStatus status;

    // Şəhər
    private String city;

    // Lead value
    @Column(name = "lead_value", precision = 15, scale = 2)
    private BigDecimal leadValue;

    // Connection (məs: Zoho CRM, Pipedrive və s.)
    private String connection;

    @Column(nullable = false, length = 150)
    private String companyName;

    // Domain
    private String domain;


    // 🔗 Task ilə əlaqə
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();


    // Bir şirkətin çoxlu lead-i ola bilər compaign lead ile elaqe
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignLead> leads;


    // Bir şirkətə bir neçə şəxs aid ola bilər (One-to-Many)
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Person> employees = new HashSet<>();


    //pipeline ile
    @OneToMany(mappedBy = "company")
    private List<Pipeline> pipelines;
}



