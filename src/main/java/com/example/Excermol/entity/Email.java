package com.example.Excermol.entity;

import com.example.Excermol.enums.EmailStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "emails")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Email {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String body;

    private String sender;

    @ElementCollection
    @CollectionTable(name = "email_recipients", joinColumns = @JoinColumn(name = "email_id"))
    @Column(name = "recipient")
    private Set<String> recipients;

    @Enumerated(EnumType.STRING)
    private EmailStatus status; // INBOX, IMPORTANT, SENT, DRAFT, SPAM, TRASH

    private boolean isRead;


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



//    @ElementCollection
//    @CollectionTable(name = "email_labels", joinColumns = @JoinColumn(name = "email_id"))
//    @Column(name = "label")
//    private List<String> labels; // Client, Work, Contest, Social media




//email ile task arasinda elaqe
//    @ManyToOne
//    @JoinColumn(name = "task_id")
//    private Task task;

//email ile organization ile elaqe
//    @ManyToOne
//    @JoinColumn(name = "organization_id")
//    private Organization organization;


//email person ile elaqe
//    @ManyToOne
//    @JoinColumn(name = "person_id")
//    private Person person;



//    formresponse ile
// Əgər form cavabları ilə əks əlaqəni görmək istəyirsənsə
//@OneToMany(mappedBy = "email")
//private List<FormResponse> responses = new ArrayList<>();




    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
