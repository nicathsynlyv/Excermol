package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
@Getter
@Setter

public class TaskRequestDto {

    @NotBlank
    private String title;

    private String description;

    private LocalDate dueDate;

    private TaskPriority priority;

    private TaskStatus status;

    @Max(100)
    private int progress;

    private Integer sortOrder;

    private Integer totalSubtasks;

    private Integer completedSubtasks;

    // RELATION IDS

    private Long companyId;

    private Long leadId;

    private Set<Long> assigneeIds;

    private Set<Long> tagIds;

}