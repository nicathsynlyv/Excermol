package com.example.Excermol.entity;

import com.example.Excermol.enums.FormStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    @Column(name = "responses_count")
    private Integer responsesCount = 0;

    //build ile elaqe
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormField> fields = new ArrayList<>();
    //response ile elaqe
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormResponse> responses = new ArrayList<>();
    //routing ile elaqe
    @OneToMany(mappedBy = "form", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FormRouting> routings = new ArrayList<>();

    @Column(name = "links")
    private String links; // form URL

    @Enumerated(EnumType.STRING)
    private FormStatus status;


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

    //    user ile
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;


}
