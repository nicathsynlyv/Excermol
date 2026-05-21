package com.example.Excermol.entity;

import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
//@Builder
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Column(name = "lead")
    private String lead;

    private int progress; // 0-100%

    @Enumerated(EnumType.STRING)
    private TaskStatus status; // TODO, IN_PROGRESS, DONE

    // Bir task-ın çoxlu "tag"ı ola bilər
    @ManyToMany
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    // Bir task-ın bir neçə user-i ola bilər
    @ManyToMany
    @JoinTable(
            name = "task_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees;


    //comments ile elaqe
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments;

    //attachments ile elaqe
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Attachment> attachments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    // 🔗 Company ilə əlaqə
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
