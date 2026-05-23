package com.example.Excermol.Service;

import com.example.Excermol.entity.Task;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService extends BaseService<Task, Long> {
    // 1. Kanban sütunları üçün
    List<Task> findByStatusOrderBySortOrderAsc(TaskStatus status);

    // 2. User taskları
    List<Task> findByAssignees_Id(Long userId);

    // 3. Search
    List<Task> findByTitleContainingIgnoreCase(String keyword);

    // 4. Priority filter
    List<Task> findByPriority(TaskPriority priority);

    // 5. Tag filter
    List<Task> findByTags_Id(Long tagId);

    // CREATE
    TaskResponseDto createTask(TaskRequestDto dto);

    // UPDATE
    TaskResponseDto updateTask(Long id, TaskRequestDto dto);


}
