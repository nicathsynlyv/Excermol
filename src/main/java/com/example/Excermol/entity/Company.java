package com.example.Excermol.entity;

import com.example.Excermol.enums.CompanyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "companies")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//
//    // Şirkət adı
//    @NotBlank(message = "Company name is required")
//    @Column(nullable = false)
//    private String name;
//
//    // Email
//    @Email(message = "Invalid email format")
//    @Column(nullable = false, unique = true)
//    private String emailAddress;
//
//    // Status (Closed, Interested, Engaged və s.)
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private CompanyStatus status;
//
//    // Şəhər
//    private String city;
//
//    // Lead dəyəri
//    @Column(precision = 15, scale = 2)
//    private BigDecimal leadValue;
//
//    // CRM connection
//    private String connection;
//
//    // Sahib (owner)
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id", nullable = false)
//    private User owner;

    // Lead person
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "lead_id")
//    private Person lead;
//
//    // Əlaqəli kampaniyalar
//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Campaign> campaigns;
//
//    // Əlaqəli şəxslər
//    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Person> persons;


    // 🔗 Task ilə əlaqə
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();


    // Bir şirkətin çoxlu lead-i ola bilər compaign lead ile elaqe
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CampaignLead> leads;
}



