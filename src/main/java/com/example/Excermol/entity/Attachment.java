package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_type")
    private String fileType;

    // Task ilə əlaqə
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = true)
    private Task task;

    // Faylı yükləyən user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User uploadedBy;

    // Email ilə əlaqə
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email_id", nullable = true)
    private Email email;
}