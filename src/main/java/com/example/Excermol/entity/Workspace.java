package com.example.Excermol.entity;

import com.example.Excermol.enums.PhoneCountryCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workspaces")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Settings -> General tab
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2,max = 100)
    @Column(name = "name", nullable = false,length = 100)
    private String name;

    @Size(max = 500)
    @Column(name = "workspace_Avatar",length = 500)
    private String workspaceAvatar;

    @Size(max = 10)
    @Column(name = "currency",length = 10)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_country_code")
    private PhoneCountryCode phoneCountryCode;  //AZERBAIJAN("+994"), TURKIYE("+90"), RUSSIA("+7"), GEORGIA("+995"), IRAN("+98");


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkspaceMember> members = new ArrayList<>();

    // Workspace-in notification settings-i
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotificationSetting> notificationSettings = new ArrayList<>();

    // Workspace-in attribute-ları
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CompanyAttribute> companyAttributes = new ArrayList<>();

    // Workspace-in integration-ları
    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Integration> integrations = new ArrayList<>();

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
