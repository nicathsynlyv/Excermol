package com.example.Excermol.entity;

import com.example.Excermol.enums.OrganizationType;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "organizations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    // (User ilə əlaqə)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;


    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // Son əlaqə vaxtı
    private LocalDateTime lastInteractedAt;

    @Enumerated(EnumType.STRING)
    private OrganizationType listName;

    // Deal sayı
    private Integer numberOfDeals;

    // İşçi sayı
    private Integer employeesRange;

    //pipeline ile
//    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
//    private List<Pipeline> pipelines;


}
