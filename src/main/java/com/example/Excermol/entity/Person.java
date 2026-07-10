package com.example.Excermol.entity;

import com.example.Excermol.enums.ConnectionStrength;
import com.example.Excermol.enums.PersonStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false, length = 50)
    private String fullName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(nullable = false, length = 50)
    private String lastName;

    // Job Title / Position
    @Size(max = 100)
    @Column(length = 100)
    private String jobTitle;

    //website ucun
    @Size(max = 255)
    private String websiteUrl;

    // Phone Number
    private String phone;

    //Social media
    @Size(max = 255)
    private String linkedinUrl;
    private String whatsappUsername;
    private String twitterName;
    private String instagramName;
    private BigDecimal leadValue;      // $7,738 for example)
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

    @jakarta.validation.constraints.Email
    @Size(max = 100)
    @Column(unique = true, length = 100)
    private String email; // məsələn: "jaman@gmail.com"


    // Activity ilə əlaqə (One-to-Many)
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonActivity> activities = new HashSet<>();


    //Person hansı user-ə məxsusdur new changes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    //form response ucun
    @OneToMany(mappedBy = "contact")
    private List<FormResponse> formResponses = new ArrayList<>();
}
