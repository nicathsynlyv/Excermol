package com.example.Excermol.entity;

import com.example.Excermol.enums.ConnectionStrength;
import com.example.Excermol.enums.PersonStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "persons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Create People popup-dan
    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false)
    private String lastName;

    // Job Title / Position
    private String jobTitle;

    //website ucun
    private String websiteUrl;

    // Phone Number
    private String phone;

    //Social media
    private String linkedinUrl;
    private String whatsappUsername;
    private String twitterName;
    private String instagramName;
    private BigDecimal  leadValue;      // $7,738 for example)
    private String lists;          // "Partnerships"
    // Connection strength (None, Strong və s.)
    @Enumerated(EnumType.STRING)
    private ConnectionStrength connectionStrength; // "None"


    // Status (Closed, Interested, Engaged...)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private PersonStatus status;

    // Last Interaction
    private LocalDateTime lastInteractionAt;

    @PrePersist
    protected void onCreate() {
        this.lastInteractionAt = LocalDateTime.now();
    }



    // --- RELATIONSHIPS ---

    // Company ilə əlaqə (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonNote> notes = new HashSet<>();


    // Tags ilə əlaqə (Many-to-Many)
    @ManyToMany
    @JoinTable(
            name = "person_tags",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new HashSet<>();

    // Emails ilə əlaqə (One-to-Many)
    @ManyToMany(mappedBy = "recipients")
    private Set<Email> receivedEmails;


    // Person entity-də — şəxsin öz email ünvanı
    @Column(unique = true)
    private String email; // məsələn: "jaman@gmail.com"



    // Activity ilə əlaqə (One-to-Many)
    @OneToMany(mappedBy = "person",cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonActivity> activities = new HashSet<>();












// gelecekde duzeltmek ucun    // Person hansı user-ə məxsusdur new changes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
