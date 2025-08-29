package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "organizations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Organization adı
    @Column(nullable = false)
    private String name;

    // Domain (website)
    @Column(nullable = false, unique = true)
    private String domain;

    // Təsvir
    @Column(length = 250)
    private String description;

    // Manager (User ilə əlaqə)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

    // Son əlaqə vaxtı
    private LocalDateTime lastInteractedAt;

    // List adı (Clients, Customer və s.)
    private String listName;

    // Deal sayı
    private Integer numberOfDeals;

    // İşçi sayı
    private Integer employeesRange;
//person ile elaqe
//    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
//    private List<Person> people;
//tasks ile elaqe
//    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
//    private List<Task> tasks;
//email ile elaqe
//    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
//    private List<Email> emails;
}
