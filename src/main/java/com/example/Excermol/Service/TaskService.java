package com.example.Excermol.Service;

import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService extends BaseService<Task,Long>{
    // Status-a görə tapmaq
    List<Task> findByStatus(TaskStatus status);

    // User-id-ə görə (assignee olan task-lar)
    List<Task> findByAssignees_Id(Long userId);

    // Tag-id-ə görə
    List<Task> findByTags_Id(Long tagId);

    List<Task> findByDueDateBetween(LocalDate start, LocalDate end);

}
