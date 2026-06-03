package com.example.Excermol.entity;

import com.example.Excermol.enums.FieldType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "form_fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label", nullable = false)
    private String label; // "Work Email Address", "First Name"

    @Column(name = "placeholder")
    private String placeholder; // "Enter email address", "Enter first name"

    @Enumerated(EnumType.STRING)
    @Column(name = "field_type", nullable = false)
    private FieldType fieldType; // TEXT, EMAIL, SELECT ...

    @Column(name = "is_required")
    private Boolean isRequired = false;

    @Column(name = "field_order")
    private Integer fieldOrder; // sahələrin sırası (1, 2, 3...)

    // Form ile elaqe
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id", nullable = false)
    private Form form;

    // FormResponseAnswer ile elaqe
    @OneToMany(mappedBy = "formField", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormResponseAnswer> answers = new ArrayList<>();

    // FormRouting ile elaqe
    @OneToMany(mappedBy = "formField", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormRouting> routings = new ArrayList<>();

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