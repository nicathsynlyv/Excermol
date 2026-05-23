package com.example.Excermol.controller;

import com.example.Excermol.Service.TaskService;
import com.example.Excermol.entity.Task;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Controller", description = "Task-larla əlaqəli bütün əməliyyatlar")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @Operation(summary = "Bütün task-ları gətirir")
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAll());
    }

    @Operation(summary = "Task-ı ID-ə görə gətirir")
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {

        return taskService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni task yaradır")
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskRequestDto dto) {

        return ResponseEntity
                .status(201)
                .body(taskService.createTask(dto));
    }

    @Operation(summary = "Task-ı update edir")
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto dto) {

        return ResponseEntity.ok(
                taskService.updateTask(id, dto)
        );
    }

    @Operation(summary = "Task-ı silir")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {

        taskService.deleteById(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Status-a görə task-ları gətirir")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(
            @PathVariable TaskStatus status) {

        return ResponseEntity.ok(
                taskService.findByStatusOrderBySortOrderAsc(status)
        );
    }

    @Operation(summary = "User-ə görə task-ları gətirir")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                taskService.findByAssignees_Id(userId)
        );
    }

    @Operation(summary = "Tag-ə görə task-ları gətirir")
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<Task>> getTasksByTag(
            @PathVariable Long tagId) {

        return ResponseEntity.ok(
                taskService.findByTags_Id(tagId)
        );
    }

    @Operation(summary = "Axtarış")
    @GetMapping("/search")
    public ResponseEntity<List<Task>> searchTasks(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                taskService.findByTitleContainingIgnoreCase(keyword)
        );
    }

    @Operation(summary = "Priority-ə görə task-ları gətirir")
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(
            @PathVariable TaskPriority priority) {

        return ResponseEntity.ok(
                taskService.findByPriority(priority)
        );
    }
}