package com.example.Excermol.entity;

import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

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

    @Id  //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Başlıq boş ola bilməz")
    @Size(min = 2, max = 100)
    @Column(nullable = false, length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")  //TEXT PostgreSQL-də uzun mətn saxlamaq üçün istifadə olunur
    private String description;

    private LocalDate dueDate;  //Task-ın son tarixidir

    // create ve update ayri ayri
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


//    @PrePersist entity database-ə ilk dəfə yazılmamışdan əvvəl işləyir.
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }


//    Entity database-də update olunmazdan əvvəl çağırılır.
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @NotNull  //null ola bilmez
    @Enumerated(EnumType.STRING) //EnumType.STRING istifadə edirəm ki, enum dəyərləri database-də öz adları ilə saxlanılsın və ordinal dəyişikliklərindən yaranan problemlərdən qaçım
    private TaskPriority priority;

    // String lead → Person-a dəyişdirildi
    @ManyToOne //Bir çox task eyni Person-a aid ola bilər
    @JoinColumn(name = "lead_id")
    private Person lead;

    @Max(100)
    @Min(0)
    private int progress; // 0-100%

    // Kanban board-da task-ların sırasını saxlamaq üçün istifadə olunur
    private Integer sortOrder;

    // UI-da "02/43" kimi göstərilir
    private Integer totalSubtasks;
    private Integer completedSubtasks;


    @NotNull
    @Enumerated(EnumType.STRING)
    private TaskStatus status; // TODO, IN_PROGRESS, DONE

    // Bir task-ın çoxlu "tag"ı ola bilər,bir taga ait bir neçə task ola bilər
    @ManyToMany
    @JoinTable(
            name = "task_tags",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags;

    // Bir task-ın bir neçə user-i ola bilər,və həmçinin bir userin bir neçə taskı ola bilər
    @ManyToMany
    @JoinTable(
            name = "task_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> assignees;


    //comments ile elaqe
    // cascade-Bu isə Task üzərində edilən bəzi əməliyyatların əlaqəli Comment-lərə də ötürülməsini təmin edir.
    // Məsələn Task silinəndə onun comment-lərinin də silinməsi üçün istifadə olunur
    // orphanRemoval = true ,Əlaqədən çıxarılmış child entity artıq parent-a aid deyilsə, database-dən də silinə bilər.
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
