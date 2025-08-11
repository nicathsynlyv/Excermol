package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String domains;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    private LocalDateTime lastInteractedAt;

    private String listName;

    private Integer numberOfDeals;

    @Column(length = 1000)
    private String description;

    private Integer employeesRange;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<Person> people;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<Email> emails;
}
