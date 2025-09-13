package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
