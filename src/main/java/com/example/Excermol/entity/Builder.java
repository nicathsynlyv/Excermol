package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "form_builders")
@Data
@NoArgsConstructor
@AllArgsConstructor
//forms page davami
public class Builder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(name = "form_title")
    private String formTitle; // "Inbound Lead Form"

    // Email relation ManyToOne olmalıdır
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id", nullable = false)
    private Email workEmail;

    //company ile elaqe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company companyName;

    @Column(name = "first_name")
    private String firstName; // "First Name" field

    @Column(name = "last_name")
    private String lastName; // "Last name" field


}
