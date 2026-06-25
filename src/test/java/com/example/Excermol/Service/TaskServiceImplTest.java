package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.TaskServiceImpl;
import com.example.Excermol.entity.*;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import com.example.Excermol.exception.TaskNotFoundException;
import com.example.Excermol.mapper.TaskMapper;
import com.example.Excermol.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskServiceImpl taskService;

    private Task task;
    private TaskRequestDto requestDto;
    private TaskResponseDto responseDto;


    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");

        requestDto = new TaskRequestDto();
        requestDto.setTitle("Test Task");
        requestDto.setStatus(TaskStatus.TODO);

        responseDto = new TaskResponseDto();
        responseDto.setId(1L);
        responseDto.setTitle("Test Task");
    }

    // =========================
    // GET ALL

    @Test
    void getAll_shouldReturnAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.getAll();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Test Task");
        verify(taskRepository).findAll();
    }


    @Test
    void getAll_shouldReturnEmptyList_whenNoTasks() {
        when(taskRepository.findAll()).thenReturn(List.of());

        List<TaskResponseDto> result = taskService.getAll();

        assertThat(result).isEmpty();
    }


    // =========================
    // GET BY ID
    // =========================
    @Test
    void getById_shouldReturnTask_whenExists() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        TaskResponseDto result = taskService.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(taskRepository).findById(1L);
    }


    @Test
    void getById_shouldThrowException_whenNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.getById(99L));
    }


    // =========================
    // CREATE
    // =========================
    @Test
    void createTask_shouldSaveAndReturnTask_withoutRelations() {
        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        TaskResponseDto result = taskService.createTask(requestDto);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test Task");
        verify(taskRepository).save(task);
    }

    @Test
    void createTask_shouldSetCompany_whenCompanyIdProvided() {
        requestDto.setCompanyId(10L);
        Company company = new Company();
        company.setId(10L);

        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        taskService.createTask(requestDto);

        assertThat(task.getCompany()).isEqualTo(company);
        verify(companyRepository).findById(10L);
    }

    @Test
    void createTask_shouldThrowException_whenCompanyNotFound() {
        requestDto.setCompanyId(10L);
        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(companyRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.createTask(requestDto));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void createTask_shouldSetLead_whenLeadIdProvided() {
        requestDto.setLeadId(20L);
        Person person = new Person();
        person.setId(20L);

        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(personRepository.findById(20L)).thenReturn(Optional.of(person));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        taskService.createTask(requestDto);

        assertThat(task.getLead()).isEqualTo(person);
    }

    @Test
    void createTask_shouldThrowException_whenLeadNotFound() {
        requestDto.setLeadId(20L);
        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(personRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.createTask(requestDto));
    }

    @Test
    void createTask_shouldSetAssignees_whenAssigneeIdsProvided() {
        requestDto.setAssigneeIds(Set.of(30L, 31L));
        User u1 = new User();
        u1.setId(30L);
        User u2 = new User();
        u2.setId(31L);

        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(userRepository.findById(30L)).thenReturn(Optional.of(u1));
        when(userRepository.findById(31L)).thenReturn(Optional.of(u2));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        taskService.createTask(requestDto);

        assertThat(task.getAssignees()).containsExactlyInAnyOrder(u1, u2);
    }

    @Test
    void createTask_shouldThrowException_whenAssigneeNotFound() {
        requestDto.setAssigneeIds(Set.of(30L));
        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(userRepository.findById(30L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.createTask(requestDto));
    }

    @Test
    void createTask_shouldSetTags_whenTagIdsProvided() {
        requestDto.setTagIds(Set.of(40L));
        Tag tag = new Tag();
        tag.setId(40L);

        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(tagRepository.findById(40L)).thenReturn(Optional.of(tag));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        taskService.createTask(requestDto);

        assertThat(task.getTags()).containsExactly(tag);
    }

    @Test
    void createTask_shouldThrowException_whenTagNotFound() {
        requestDto.setTagIds(Set.of(40L));
        when(taskMapper.toEntity(requestDto)).thenReturn(task);
        when(tagRepository.findById(40L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> taskService.createTask(requestDto));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateTask_shouldUpdateFieldsAndReturnTask() {
        requestDto.setTitle("Updated Title");
        requestDto.setDescription("Updated Desc");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        taskService.updateTask(1L, requestDto);

        assertThat(task.getTitle()).isEqualTo("Updated Title");
        assertThat(task.getDescription()).isEqualTo("Updated Desc");
        verify(taskRepository).save(task);
    }

    @Test
    void updateTask_shouldThrowException_whenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(99L, requestDto));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void updateTask_shouldUpdateCompany_whenCompanyIdProvided() {
        requestDto.setCompanyId(10L);
        Company company = new Company();
        company.setId(10L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        taskService.updateTask(1L, requestDto);

        assertThat(task.getCompany()).isEqualTo(company);
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void delete_shouldDeleteTask_whenExists() {
        when(taskRepository.existsById(1L)).thenReturn(true);

        taskService.delete(1L);

        verify(taskRepository).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenNotFound() {
        when(taskRepository.existsById(99L)).thenReturn(false);

        assertThrows(TaskNotFoundException.class, () -> taskService.delete(99L));
        verify(taskRepository, never()).deleteById(any());
    }

    // =========================
    // FILTERS
    // =========================
    @Test
    void findByStatus_shouldReturnFilteredTasks() {
        when(taskRepository.findByStatusOrderBySortOrderAsc(TaskStatus.TODO)).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.findByStatus(TaskStatus.TODO);

        assertThat(result).hasSize(1);
        verify(taskRepository).findByStatusOrderBySortOrderAsc(TaskStatus.TODO);
    }

    @Test
    void findByUser_shouldReturnTasksForUser() {
        when(taskRepository.findByAssignees_Id(5L)).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.findByUser(5L);

        assertThat(result).hasSize(1);
        verify(taskRepository).findByAssignees_Id(5L);
    }

    @Test
    void search_shouldReturnMatchingTasks() {
        when(taskRepository.findByTitleContainingIgnoreCase("test")).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.search("test");

        assertThat(result).hasSize(1);
        verify(taskRepository).findByTitleContainingIgnoreCase("test");
    }

    @Test
    void findByPriority_shouldReturnFilteredTasks() {
        when(taskRepository.findByPriority(TaskPriority.HIGH)).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.findByPriority(TaskPriority.HIGH);

        assertThat(result).hasSize(1);
        verify(taskRepository).findByPriority(TaskPriority.HIGH);
    }

    @Test
    void findByTag_shouldReturnFilteredTasks() {
        when(taskRepository.findByTags_Id(7L)).thenReturn(List.of(task));
        when(taskMapper.toResponse(task)).thenReturn(responseDto);

        List<TaskResponseDto> result = taskService.findByTag(7L);

        assertThat(result).hasSize(1);
        verify(taskRepository).findByTags_Id(7L);
    }


}
