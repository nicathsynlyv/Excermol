package com.example.Excermol.entity;

import com.example.Excermol.enums.IntegrationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "integrations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Settings -> Integrations tab
public class Integration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ZAPIER, MAKE, CHROME, SLACK, MAILCHIMP, DROPBOX, ASANA, EVERNOTE
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "integration_type", nullable = false)
    private IntegrationType integrationType;

    // "Automate Breakcold with 6000+ applications"
    @Size(max = 500)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    // aktiv/deaktiv - toggle
    @Column(name = "is_active")
    private Boolean isActive = false;

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
