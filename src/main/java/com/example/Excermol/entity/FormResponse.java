package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//forms page davami
public class FormResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    // Form ile elaqe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    // Connected Contact - People modulundaki movcud contact ile elaqe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id")
    private Person contact;

    // her cavab field-lerin deyerlerini saxlayir
    @OneToMany(mappedBy = "formResponse", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormResponseAnswer> answers = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        submittedAt = LocalDateTime.now();
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
