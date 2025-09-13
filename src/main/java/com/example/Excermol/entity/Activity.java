package com.example.Excermol.entity;

import com.example.Excermol.enums.ActivityAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
//Bu class Person classina uygun yazilir  aktive adamlar ucun
public class Activity {

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

    // Əlaqəli şəxs
    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false)
    private Person person;
}
