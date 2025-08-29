package com.example.Excermol.controller;

import com.example.Excermol.Service.TaskService;
import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskStatus;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    // ✅ Bütün task-lar
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    // ✅ Status-a görə task-lar
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable TaskStatus status) {
        return ResponseEntity.ok(taskService.getTasksByStatus(status));
    }

    // ✅ User-ə görə task-lar
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUser(userId));
    }

    // ✅ Tag-ə görə task-lar
    @GetMapping("/tag/{tagId}")
    public ResponseEntity<List<Task>> getTasksByTag(@PathVariable Long tagId) {
        return ResponseEntity.ok(taskService.getTasksByTag(tagId));
    }

    // ✅ Tarix aralığına görə task-lar
    @GetMapping("/due")
    public ResponseEntity<List<Task>> getTasksByDueDate(@RequestParam LocalDate start,
                                                        @RequestParam LocalDate end) {
        return ResponseEntity.ok(taskService.getTasksByDueDateRange(start, end));
    }

    // ✅ Yeni task yaratmaq
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task createdTask = taskService.createTask(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    // ✅ Task update
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        Task updated = taskService.updateTask(id, task);
        return ResponseEntity.ok(updated);
    }

    // ✅ Task silmək
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
