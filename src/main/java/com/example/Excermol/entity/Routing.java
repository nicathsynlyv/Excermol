package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "form_routings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Routing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(name = "routing_title")
    private String routingTitle; // "Routing Lead Form" basliq ucun

    // Email ilə əlaqə (One-to-One)
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "email_id", nullable = false)
    private Email workEmail;

    @Column(name = "contains")
    private String contains; // "Contains"

    @Column(name = "then_redirect_to")
    private String thenRedirectTo; // "Thank you page"

    // Company ilə əlaqə (Many-to-One)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company companyName;
}
