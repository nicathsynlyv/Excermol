package com.example.Excermol.entity;

import com.example.Excermol.enums.ActivityAction;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//Bu class Person classina uygun yazilir  aktive adamlar ucun
public class PersonActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Activity type (Created, Updated, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ActivityAction action;

    // Kim tərəfindən edilib (user adı və ya id ola bilər)
    private String performedBy;

    // Vaxt
    private LocalDateTime performedAt;
    @PrePersist
    protected void onCreate() {
        this.performedAt = LocalDateTime.now();
    }

    // Əlaqəli şəxs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
