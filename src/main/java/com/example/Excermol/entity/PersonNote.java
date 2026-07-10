package com.example.Excermol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @Size(max = 5000)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Kimin adından yazılıb
    @Email
    @Size(max = 100)
    @Column(length = 100)
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
