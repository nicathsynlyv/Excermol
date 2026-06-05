package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// Settings -> Notifications tab
public class NotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // 3 dene checkbox var

    // Any new comment mode - checkbox
    @Column(name = "any_new_comment")
    private Boolean anyNewComment = false;

    // You are assigned to an organization - checkbox
    @Column(name = "assigned_to_organization")
    private Boolean assignedToOrganization = false;

    // You are following an organization - checkbox
    @Column(name = "following_organization")
    private Boolean followingOrganization = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;


    @Column(name = "update_at")
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
