package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.TaskServiceImpl;
import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Controller", description = "Task-larla əlaqəli bütün əməliyyatlar")
public class TaskController {

    private final TaskServiceImpl taskServiceImpl;

    public TaskController(TaskServiceImpl taskServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
    }

    @Operation(summary = "Bütün task-ları gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bütün task-lar uğurla gətirildi")
    })
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskServiceImpl.getAll());
    }

    @Operation(summary = "Task-ı ID-ə görə gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task tapıldı"),
            @ApiResponse(responseCode = "404", description = "Task tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni task yaradır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Task uğurla yaradıldı")
    })
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskServiceImpl.save(task);
        return ResponseEntity.status(201).body(savedTask);
    }

    @Operation(summary = "Task-ı ID-ə görə update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task uğurla update olundu"),
            @ApiResponse(responseCode = "404", description = "Task tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task updatedTask = taskServiceImpl.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }


    @Operation(summary = "Task-ı ID-ə görə silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Task tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskServiceImpl.getById(id).isPresent()) {
            taskServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Status-a görə task-ları gətirir")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskServiceImpl.findByStatus(status));
    }

    @Operation(summary = "User ID-yə görə task-ları gətirir")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskServiceImpl.findByAssignees_Id(userId));
    }

    @Operation(summary = "Tag ID-yə görə task-ları gətirir")
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<Task>> getTasksByTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(taskServiceImpl.findByTags_Id(tagId));
    }

    @Operation(summary = "Tarix aralığına görə task-ları gətirir")
    @GetMapping("/dueDate")
    public ResponseEntity<List<Task>> getTasksByDueDateRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return ResponseEntity.ok(taskServiceImpl.findByDueDateBetween(startDate, endDate));
    }
}
