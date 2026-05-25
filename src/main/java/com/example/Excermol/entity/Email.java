package com.example.Excermol.entity;

import com.example.Excermol.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "emails")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Email {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;


    // String Set əvəzinə:
    @ManyToMany
    @JoinTable(name = "email_recipients",
            joinColumns = @JoinColumn(name = "email_id"),
            inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> recipients;

    @Enumerated(EnumType.STRING)
    private EmailStatus status; // INBOX, IMPORTANT, SENT, DRAFT, SPAM, TRASH

    private boolean read;


    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    private LocalDateTime updatedAt;

    //attachments ile elaqe
    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    //email ve compaign ile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;


    @ElementCollection
    @CollectionTable(name = "email_labels", joinColumns = @JoinColumn(name = "email_id"))
    @Column(name = "label")
    private List<String> labels; // Client, Work, Contest, Social media



    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
    // String əvəzinə:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    //company ile
    // Düzgünü:
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

}
