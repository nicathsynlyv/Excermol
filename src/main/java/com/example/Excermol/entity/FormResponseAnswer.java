package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "form_response_answers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormResponseAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "value", columnDefinition = "TEXT")
    private String value; // "Tahsan", "tahsankhan@gmail.com", "Zoho CRM"

    // Hansi FormResponse-a aiddir
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_response_id", nullable = false)
    private FormResponse formResponse;

    // Hansi FormField-e aiddir
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_field_id", nullable = false)
    private FormField formField;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
