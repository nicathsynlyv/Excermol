package com.example.Excermol.entity;

import com.example.Excermol.enums.PersonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lead Name
    @Column(nullable = false, length = 120)
    private String fullName;

    // Job Title / Position
    private String jobTitle;

    // Status (Closed, Interested, Engaged...)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PersonStatus status;

    // Last Interaction
    private LocalDateTime lastInteraction;

    // Phone Number
    private String phone;

    // Website
    private String websiteUrl;

    // Social Media
    private String linkedInUrl;
    private String whatsappUsername;
    private String twitterHandle;
    private String instagramHandle;

    // Lead Value ($...)
    private BigDecimal leadValue;

    // Connection strength (None, Strong və s.)
    private String connectionStrength;

    // --- RELATIONSHIPS ---

    // Company ilə əlaqə (Many-to-One)
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    // Tags ilə əlaqə (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "person_tags",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Emails ilə əlaqə (One-to-Many)
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Email> emails = new HashSet<>();

    // Activity ilə əlaqə (One-to-Many)
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Activity> activities = new HashSet<>();
}
