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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public TaskServiceImpl(TaskRepository taskRepository, CompanyRepository companyRepository, PersonRepository personRepository, UserRepository userRepository, TagRepository tagRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.companyRepository = companyRepository;
        this.personRepository = personRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.taskMapper = taskMapper;
    }

    @Override
    public List<Task> getAll() {
        return taskRepository.findAll();
    }

    @Override
    public Optional<Task> getById(Long id) {
        return taskRepository.findById(id);
    }

    @Override
    public Task save(Task entity) {
        return taskRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {

        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException("Task tapılmadı: " + id);
        }

        taskRepository.deleteById(id);
    }

    @Override
    public List<Task> findByStatusOrderBySortOrderAsc(TaskStatus status) {
        return taskRepository.findByStatusOrderBySortOrderAsc(status);
    }

    @Override
    public List<Task> findByAssignees_Id(Long userId) {
        return taskRepository.findByAssignees_Id(userId);
    }

    @Override
    public List<Task> findByTitleContainingIgnoreCase(String keyword) {
        return taskRepository.findByTitleContainingIgnoreCase(keyword);
    }

    @Override
    public List<Task> findByPriority(TaskPriority priority) {
        return taskRepository.findByPriority(priority);
    }

    @Override
    public List<Task> findByTags_Id(Long tagId) {
        return taskRepository.findByTags_Id(tagId);
    }

    @Override
    public TaskResponseDto createTask(TaskRequestDto dto) {

        Task task = taskMapper.toEntity(dto);

        // COMPANY
        if (dto.getCompanyId() != null) {

            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() ->
                            new RuntimeException("Company not found"));

            task.setCompany(company);
        }

        // LEAD
        if (dto.getLeadId() != null) {

            Person lead = personRepository.findById(dto.getLeadId())
                    .orElseThrow(() ->
                            new RuntimeException("Lead not found"));

            task.setLead(lead);
        }

        // USERS
        if (dto.getAssigneeIds() != null) {

            Set<User> users = dto.getAssigneeIds()
                    .stream()
                    .map(id -> userRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("User not found")))
                    .collect(Collectors.toSet());

            task.setAssignees(users);
        }

        // TAGS
        if (dto.getTagIds() != null) {

            Set<Tag> tags = dto.getTagIds()
                    .stream()
                    .map(id -> tagRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("Tag not found")))
                    .collect(Collectors.toSet());

            task.setTags(tags);
        }

        Task savedTask = taskRepository.save(task);

        return taskMapper.toResponse(savedTask);
    }

    @Override
    public TaskResponseDto updateTask(Long id, TaskRequestDto dto) {

        Task existing = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException("Task tapılmadı: " + id));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());

        existing.setDueDate(dto.getDueDate());

        existing.setPriority(dto.getPriority());
        existing.setStatus(dto.getStatus());

        existing.setProgress(dto.getProgress());

        existing.setSortOrder(dto.getSortOrder());

        existing.setTotalSubtasks(dto.getTotalSubtasks());
        existing.setCompletedSubtasks(dto.getCompletedSubtasks());

        // COMPANY
        if (dto.getCompanyId() != null) {

            Company company = companyRepository.findById(dto.getCompanyId())
                    .orElseThrow(() ->
                            new RuntimeException("Company not found"));

            existing.setCompany(company);
        }

        // LEAD
        if (dto.getLeadId() != null) {

            Person lead = personRepository.findById(dto.getLeadId())
                    .orElseThrow(() ->
                            new RuntimeException("Lead not found"));

            existing.setLead(lead);
        }

        // USERS
        if (dto.getAssigneeIds() != null) {

            Set<User> users = dto.getAssigneeIds()
                    .stream()
                    .map(userId -> userRepository.findById(userId)
                            .orElseThrow(() ->
                                    new RuntimeException("User not found")))
                    .collect(Collectors.toSet());

            existing.setAssignees(users);
        }

        // TAGS
        if (dto.getTagIds() != null) {

            Set<Tag> tags = dto.getTagIds()
                    .stream()
                    .map(tagId -> tagRepository.findById(tagId)
                            .orElseThrow(() ->
                                    new RuntimeException("Tag not found")))
                    .collect(Collectors.toSet());

            existing.setTags(tags);
        }

        Task updatedTask = taskRepository.save(existing);

        return taskMapper.toResponse(updatedTask);
    }
}
