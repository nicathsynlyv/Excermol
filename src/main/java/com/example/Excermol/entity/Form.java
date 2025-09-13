package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "forms_name")
    private String formsName; // "Lead Capture Form", "Customer Onboarding Form"

    //response ile elaqe
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Response> responses;

    @Column(name = "links")
    private String links; // form URL

    @Column(name = "post")
    private Boolean post; // Publish status (true/false)

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relation Builder ile
    @OneToOne(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Builder builder;

    //Relation Routing ile
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Routing> routings;



    //user ile
//    @ManyToOne
//    @JoinColumn(name = "owner_id")
//    private User owner;


}
