package com.example.Excermol.entity;

import com.example.Excermol.enums.EmailFolder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "emails")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Email {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fromAddress;
    private String toAddress;
    private String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private EmailFolder folder; // INBOX, IMPORTANT, SENT, DRAFT, SPAM, TRASH

    @ElementCollection
    @CollectionTable(name = "email_labels", joinColumns = @JoinColumn(name = "email_id"))
    @Column(name = "label")
    private List<String> labels; // Client, Work, Contest, Social media

    private boolean read;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EmailAttachment> attachments = new ArrayList<>();

//email ile task arasinda elaqe
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
//email ile organization ile elaqe
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;


//email person ile elaqe
    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;


    // Orijinal email ID (Reply/Forward üçün)
    private Long parentEmailId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
