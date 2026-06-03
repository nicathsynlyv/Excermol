package com.example.Excermol.entity;

import com.example.Excermol.enums.RoutingCondition;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "form_routings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormRouting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Hansı şərt - "Contains", "Equals", "Starts With"
    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type")
    private RoutingCondition conditionType;

    // Şərtin dəyəri - məs. "@gmail.com", "@company.com"
    @Column(name = "condition_value")
    private String conditionValue;

    // Hara yönləndir - "Thank you page" və ya URL
    @Column(name = "redirect_to")
    private String redirectTo;

    // Company email adı
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id")
    private Email email;


    // Routing sırası
    @Column(name = "routing_order")
    private Integer routingOrder;

    // Hansi Form-a aiddir
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    // Hansi FormField-i trigger edir - "Work Email Address"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_field_id", nullable = false)
    private FormField formField;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
