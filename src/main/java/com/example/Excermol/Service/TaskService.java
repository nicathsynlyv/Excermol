package com.example.Excermol.Service;

import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskStatus;
import com.example.Excermol.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // Bütün task-ları gətir
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // Statusa görə task-ları gətir
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    // User-ə görə task-ları gətir
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByAssignees_Id(userId);
    }

    // Tag-ə görə task-ları gətir
    public List<Task> getTasksByTag(Long tagId) {
        return taskRepository.findByTags_Id(tagId);
    }

    // Tarix aralığına görə (Upcoming Tasks üçün)
    public List<Task> getTasksByDueDateRange(LocalDate start, LocalDate end) {
        return taskRepository.findByDueDateBetween(start, end);
    }

    // Yeni task yaratmaq
    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    // Task update
    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task tapılmadı!"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setProgress(updatedTask.getProgress());
        task.setStatus(updatedTask.getStatus());
        task.setTags(updatedTask.getTags());
        task.setAssignees(updatedTask.getAssignees());

        return taskRepository.save(task);
    }

    // Task silmək
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
