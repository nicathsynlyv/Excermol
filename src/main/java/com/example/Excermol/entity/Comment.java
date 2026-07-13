package com.example.Excermol.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 5000)
    @Column(columnDefinition = "TEXT")
    private String text;

    private LocalDateTime createdAt;

    // Comment bir task-a bağlıdır
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    // Comment bir user tərəfindən yazılır
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
}
