package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "person_notes")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonNote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Note məzmunu — "Hello, Designer", "Lead Qualification Summary"
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Kimin adından yazılıb
    private String authorEmail;

    // Nə vaxt yazılıb
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Əlaqəli şəxs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
