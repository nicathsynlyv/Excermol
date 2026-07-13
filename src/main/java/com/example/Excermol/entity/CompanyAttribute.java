package com.example.Excermol.entity;

import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name ="company_attributes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Settings -> Company tab
public class CompanyAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "Email", "Phone", "LinkedIn Company URL"
    @NotBlank
    @Size(min = 2, max = 100)
    @Column(name = "name", nullable = false,length = 100)
    private String name;

    // EMAIL, TEXT, LINKEDIN, WHATSAPP, TWITTER
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "attribute_type", nullable = false)
    private AttributeType attributeType;

    // SYSTEM, CUSTOM
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "attribute_property", nullable = false)
    private AttributeProperty attributeProperty;


    // Hansı workspace-ə aiddir
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

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
