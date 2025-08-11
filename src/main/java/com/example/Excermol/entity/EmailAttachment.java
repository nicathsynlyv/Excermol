package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "email_attachments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAttachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String fileType;
    private long fileSize; // bayt olaraq

    @Lob
    private byte[] fileData;

    @ManyToOne
    @JoinColumn(name = "email_id")
    private Email email;
}
