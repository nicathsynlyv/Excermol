package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.Value;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter

public class TaskRequestDto {

    @NotBlank(message = "Başlıq boş ola bilməz")
    @Size(min = 2, max = 100, message = "Başlıq 2-100 simvol arasında olmalıdır")
    private String title;

    @Size(max = 2000, message = "Təsvir 2000 simvoldan çox ola bilməz")
    private String description;

    private LocalDate dueDate;


    @NotBlank(message = "Priority boş ola bilməz")
    private TaskPriority priority;

    @NotBlank(message = "Status boş ola bilməz")
    private TaskStatus status;

    @Min(value = 0, message = "Progress mənfi ola bilməz")
    @Max(value = 100, message = "Progess 100 dən çox ola bilməz")
    private int progress;


    @Min(value = 0, message = "Sort order mənfi ola bilməz")
    private Integer sortOrder;


    @Min(value = 0, message = "Ümumi subtask sayı mənfi ola bilməz")
    private Integer totalSubtasks;

    @Min(value = 0, message = "Tamamlanan subtask sayı mənfi ola bilməz")
    private Integer completedSubtasks;

    // RELATION IDS

    private Long companyId;

    private Long leadId;

    private Set<Long> assigneeIds;

    private Set<Long> tagIds;

}