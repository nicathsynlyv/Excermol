package com.example.Excermol.controller;

import com.example.Excermol.Service.TaskService;
import com.example.Excermol.entity.Task;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/tasks")
@Tag(name = "Task Controller", description = "Task CRUD və filter əməliyyatları")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // =========================
    // CREATE
    // =========================
    @Operation(summary = "Yeni task yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "Validation xətası")
    })
    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(
            @Valid @RequestBody TaskRequestDto dto
    ) {
        return ResponseEntity.ok(taskService.createTask(dto));
    }

    // =========================
    // GET ALL
    // =========================
    @Operation(summary = "Bütün taskları gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tasklar uğurla qaytarıldı")
    })
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getAll() {
        return ResponseEntity.ok(taskService.getAll());
    }

    // =========================
    // GET BY ID
    // =========================
    @Operation(summary = "ID-ə görə task gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task tapıldı"),
            @ApiResponse(responseCode = "404", description = "Task tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getById(id));
    }

    // =========================
    // UPDATE
    // =========================
    @Operation(summary = "Task update et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Task yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Task tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> update(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto dto
    ) {
        return ResponseEntity.ok(taskService.updateTask(id, dto));
    }

    // =========================
    // DELETE
    // =========================
    @Operation(summary = "Task sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Task silindi"),
            @ApiResponse(responseCode = "404", description = "Task tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // FILTERS
    // =========================

    @Operation(summary = "Status-a görə tasklar")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponseDto>> getByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.findByStatus(status));
    }

    @Operation(summary = "User-ə görə tasklar")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponseDto>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.findByUser(userId));
    }

    @Operation(summary = "Keyword ilə axtarış")
    @GetMapping("/search")
    public ResponseEntity<List<TaskResponseDto>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(taskService.search(keyword));
    }

    @Operation(summary = "Priority filter")
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponseDto>> getByPriority(@PathVariable TaskPriority priority) {
        return ResponseEntity.ok(taskService.findByPriority(priority));
    }

    @Operation(summary = "Tag ilə filter")
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<TaskResponseDto>> getByTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(taskService.findByTag(tagId));
    }
}