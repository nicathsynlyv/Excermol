package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "form_responses")
@Data
@NoArgsConstructor
@AllArgsConstructor
//forms page davami
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    @Column(name = "first_name")
    private String firstName; // "Tahsan", "Helina", "James"

    @Column(name = "submitted_at")
    private String submittedAt; // "Zoho CRM", "Salesforce Starter", "HubSpot CRM"

    @Column(name = "connected_contact")
    private String connectedContact; // "Zoho CRM", "Insightly", "Keap"

    @Column(name = "last_name")
    private String lastName; // "Khan", "Willey", "Helin"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id")
    private Email email; // Email entity ilə relation

}
