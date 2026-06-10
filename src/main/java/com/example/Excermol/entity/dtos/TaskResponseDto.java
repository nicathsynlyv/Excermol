package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
@Getter
@Setter
public class TaskResponseDto {
    private Long id;

    private String title;

    private String description;

    private LocalDate dueDate;

    private TaskPriority priority;

    private TaskStatus status;

    private int progress;

    private Integer sortOrder;

    private Integer totalSubtasks;

    private Integer completedSubtasks;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // relation məlumatları

    private String companyName;

    private String leadName;

    private Set<String> assigneeNames;

    private Set<String> tagNames;

    private Set<Long> tagIds;

    // comments
    private Integer commentsCount;

    // email attachment ucn
//    private Integer attachmentsCount;



}
