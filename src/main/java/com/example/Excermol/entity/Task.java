package com.example.Excermol.entity;

import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Başlıq boş ola bilməz")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate dueDate;
    // create ve update ayri ayri
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    // String lead → Person-a dəyişdirildi
    @ManyToOne
    @JoinColumn(name = "lead_id")
    private Person lead;

    @Max(100)
    @Min(0)
    private int progress; // 0-100%

    // Kanban sütununda sıra üçün
    private Integer sortOrder;

    // UI-da "02/43" kimi göstərilir
    private Integer totalSubtasks;
    private Integer completedSubtasks;


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


    // 🔗 Company ilə əlaqə
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

}
