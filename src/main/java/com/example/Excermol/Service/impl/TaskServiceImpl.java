package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.TaskService;
import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskStatus;
import com.example.Excermol.exception.TaskNotFoundException;
import com.example.Excermol.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // ===== BaseService CRUD əməliyyatları =====
    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void deleteById(Long id) {
        taskRepository.deleteById(id);
    }

    // ===== TaskService xüsusi metodları =====
    @Override
    public List<Task> findByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }

    @Override
    public List<Task> findByAssignees_Id(Long userId) {
        return taskRepository.findByAssignees_Id(userId);
    }

    @Override
    public List<Task> findByTags_Id(Long tagId) {
        return taskRepository.findByTags_Id(tagId);
    }

    @Override
    public List<Task> findByDueDateBetween(LocalDate start, LocalDate end) {
        return taskRepository.findByDueDateBetween(start, end);
    }

    // Optional: Task update üçün helper metodu
    public Task updateTask(Long id, Task updatedTask) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task ID = " + id + " tapılmadı!"));

        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setDueDate(updatedTask.getDueDate());
        task.setPriority(updatedTask.getPriority());
        task.setLead(updatedTask.getLead());
        task.setProgress(updatedTask.getProgress());
        task.setStatus(updatedTask.getStatus());
        task.setTags(updatedTask.getTags());
        task.setAssignees(updatedTask.getAssignees());
        task.setCompany(updatedTask.getCompany());
        task.setAttachments(updatedTask.getAttachments());
        task.setComments(updatedTask.getComments());
        task.setUpdatedAt(updatedTask.getUpdatedAt());

        return taskRepository.save(task);
    }
}
