package com.example.Excermol.Service;

import com.example.Excermol.entity.Task;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto dto);

    TaskResponseDto updateTask(Long id, TaskRequestDto dto);

    TaskResponseDto getById(Long id);

    List<TaskResponseDto> getAll();

    List<TaskResponseDto> findByStatus(TaskStatus status);

    List<TaskResponseDto> findByUser(Long userId);

    List<TaskResponseDto> search(String keyword);

    List<TaskResponseDto> findByPriority(TaskPriority priority);

    List<TaskResponseDto> findByTag(Long tagId);

    void delete(Long id);


}
