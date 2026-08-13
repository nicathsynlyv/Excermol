package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.TaskService;
import com.example.Excermol.entity.*;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import com.example.Excermol.exception.*;
import com.example.Excermol.mapper.TaskMapper;
import com.example.Excermol.repository.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service  //Bu class service layer-ə aiddir və Spring Bean kimi idarə olunmalıdır
@Transactional // Metod daxilində database əməliyyatlarının bir transaction daxilində işləməsini təmin edir, problem yaransa rollback ede biler
@Slf4j  // Lombok avtomatik log obyekti yaradır
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CompanyRepository companyRepository;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final TaskMapper taskMapper;


    // Constructor Injection
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

    // GET ALL
    @Override
    public List<TaskResponseDto> getAll() {
        log.info("Fetching all tasks");

        List<TaskResponseDto> tasks = taskRepository.findAll()
                .stream()
                .map(taskMapper::toResponse)
                .toList();

        log.info("Retrieved {} tasks", tasks.size());

        return tasks;
    }

    // GET BY ID
    @Override
    public TaskResponseDto getById(Long id) {
        log.info("Fetching task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found with id: {}", id);
                    return new TaskNotFoundException("Task tapılmadı: " + id);
                });

        return taskMapper.toResponse(task);
    }

    // CREATE Task
    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {
        log.info("Creating task with title: {}", dto.getTitle());

        Task task = taskMapper.toEntity(dto);
        setRelations(task, dto);

        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());

        return taskMapper.toResponse(savedTask);
    }

    // UPDATE
    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {
        log.info("Updating task with id: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Task not found for update. Id: {}", id);
                    return new TaskNotFoundException("Task tapılmadı: " + id);
                });

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setProgress(dto.getProgress());
        task.setSortOrder(dto.getSortOrder());
        task.setTotalSubtasks(dto.getTotalSubtasks());
        task.setCompletedSubtasks(dto.getCompletedSubtasks());
//        relationlar yenilenir
        setRelations(task, dto);

        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully. Id: {}", id);

        return taskMapper.toResponse(updatedTask);
    }

    // DELETE
    @Override
    public void delete(Long id) {
        log.info("Deleting task with id: {}", id);

        if (!taskRepository.existsById(id)) {
            log.warn("Task not found for deletion. Id: {}", id);
            throw new TaskNotFoundException("Task tapılmadı: " + id);
        }

        taskRepository.deleteById(id);
        log.info("Task deleted successfully. Id: {}", id);
    }

    // =========================
    // FILTERS
    // =========================
    @Override
    public List<TaskResponseDto> findByStatus(TaskStatus status) {
        log.info("Fetching tasks by status: {}", status);
        return taskRepository.findByStatusOrderBySortOrderAsc(status)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

//    Müəyyən User-ə assign edilmiş Task-ları tapır.
    @Override
    public List<TaskResponseDto> findByUser(Long userId) {
        log.info("Fetching tasks for user id: {}", userId);
        return taskRepository.findByAssignees_Id(userId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

//    Title daxilində keyword axtarır.
    @Override
    public List<TaskResponseDto> search(String keyword) {
        log.info("Searching tasks with keyword: {}", keyword);
        return taskRepository.findByTitleContainingIgnoreCase(keyword)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

//    Priority-yə görə filter edir.
    @Override
    public List<TaskResponseDto> findByPriority(TaskPriority priority) {
        log.info("Fetching tasks by priority: {}", priority);
        return taskRepository.findByPriority(priority)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

//    Tag ID-yə görə filter edir.
    @Override
    public List<TaskResponseDto> findByTag(Long tagId) {
        log.info("Fetching tasks by tag id: {}", tagId);
        return taskRepository.findByTags_Id(tagId)
                .stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    // =========================
    // RELATION HANDLER (IMPORTANT)
    // =========================
//    Request DTO-da gələn ID-ləri real Entity obyektlərinə çevirmək və Task-a bağlamaq.
    private void setRelations(Task task, TaskRequestDto dto) {

        if (dto.getCompanyId() != null) {
            task.setCompany(
                    companyRepository.findById(dto.getCompanyId())
                            .orElseThrow(() -> {
                                log.warn("Company not found with id: {}", dto.getCompanyId());
                                return new CompanyNotFoundException("Company tapılmadı");
                            })
            );
        }

        if (dto.getLeadId() != null) {
            task.setLead(
                    personRepository.findById(dto.getLeadId())
                            .orElseThrow(() -> {
                                log.warn("Lead not found with id: {}", dto.getLeadId());
                                return new PersonNotFoundException("Lead tapılmadı");
                            })
            );
        }

        if (dto.getAssigneeIds() != null) {
            task.setAssignees(
                    dto.getAssigneeIds().stream()
                            .map(id -> userRepository.findById(id)
                                    .orElseThrow(() -> {
                                        log.warn("User not found with id: {}", id);
                                        return new UserNotFoundException("User tapılmadı");
                                    }))
                            .collect(Collectors.toSet())
            );
        }

        if (dto.getTagIds() != null) {
            task.setTags(
                    dto.getTagIds().stream()
                            .map(id -> tagRepository.findById(id)
                                    .orElseThrow(() -> {
                                        log.warn("Tag not found with id: {}", id);
                                        return new TagNotFoundException("Tag tapılmadı");
                                    }))
                            .collect(Collectors.toSet())
            );
        }
    }
}

// Task digər entity-lərlə relation-a malikdir. Request-də relation-lar ID formasında gəldiyi üçün Service layer həmin
// ID-lər vasitəsilə Company, Person, User və Tag entity-lərini Repository-dən tapır və Task-a bağlayır
// Mən bunu setRelations() metodunda ayrıca ayırmışam ki, əsas CRUD metodlarında kod təkrarı yaranmasın