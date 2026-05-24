package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.TaskService;
import com.example.Excermol.entity.*;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import com.example.Excermol.exception.TaskNotFoundException;
import com.example.Excermol.mapper.TaskMapper;
import com.example.Excermol.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TaskMapper taskMapper;

    public TaskServiceImpl(
            TaskRepository taskRepository,
            CompanyRepository companyRepository,
            PersonRepository personRepository,
            UserRepository userRepository,
            TagRepository tagRepository,
            TaskMapper taskMapper
    ) {
        this.taskRepository = taskRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.taskMapper = taskMapper;
    }

    // =========================
    // GET ALL
    // =========================
    @Override
    public List<TaskResponseDto> getAll() {
        return taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    // =========================
    // GET BY ID
    // =========================
    @Override
    public TaskResponseDto getById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: " + id));

        return taskMapper.toResponse(task);
    }

    // =========================
    // CREATE
    // =========================
    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {

        Task task = taskMapper.toEntity(dto);

        setRelations(task, dto);

        return taskMapper.toResponse(taskRepository.save(task));
    }

    // =========================
    // UPDATE
    // =========================
    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: " + id));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setProgress(dto.getProgress());
        task.setSortOrder(dto.getSortOrder());
        task.setTotalSubtasks(dto.getTotalSubtasks());
        task.setCompletedSubtasks(dto.getCompletedSubtasks());

        setRelations(task, dto);

        return taskMapper.toResponse(taskRepository.save(task));
    }

    // =========================
    // DELETE
    // =========================
    @Override
    public void delete(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task tapılmadı: " + id);
        }
        taskRepository.deleteById(id);
    }

    // =========================
    // FILTERS
    // =========================

    @Override
    public List<TaskResponseDto> findByStatus(TaskStatus status) {
        return taskRepository.findByStatusOrderBySortOrderAsc(status)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponseDto> findByUser(Long userId) {
        return taskRepository.findByAssignees_Id(userId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponseDto> search(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponseDto> findByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<TaskResponseDto> findByTag(Long tagId) {
        return taskRepository.findByTags_Id(tagId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    // =========================
    // RELATION HANDLER (IMPORTANT)
    // =========================
    private void setRelations(Task task, TaskRequestDto dto) {

        if (dto.getCompanyId() != null) {
            task.setCompany(
                    companyRepository.findById(dto.getCompanyId())
                            .orElseThrow(() -> new RuntimeException("Company not found"))
            );
        }

        if (dto.getLeadId() != null) {
            task.setLead(
                    personRepository.findById(dto.getLeadId())
                            .orElseThrow(() -> new RuntimeException("Lead not found"))
            );
        }

        if (dto.getAssigneeIds() != null) {
            task.setAssignees(
                    dto.getAssigneeIds().stream()
                            .map(id -> userRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("User not found")))
                            .collect(Collectors.toSet())
            );
        }

        if (dto.getTagIds() != null) {
            task.setTags(
                    dto.getTagIds().stream()
                            .map(id -> tagRepository.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Tag not found")))
                            .collect(Collectors.toSet())
            );
        }
    }
}