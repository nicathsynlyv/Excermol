package com.example.Excermol.Service;
import com.example.Excermol.Service.impl.TagServiceImpl;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.Tag;
import com.example.Excermol.entity.Task;
import com.example.Excermol.entity.dtos.TagCreateRequestDTO;
import com.example.Excermol.entity.dtos.TagResponseDTO;
import com.example.Excermol.entity.dtos.TagUpdateRequestDTO;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.exception.TagAlreadyExistsException;
import com.example.Excermol.exception.TagNotFoundException;
import com.example.Excermol.exception.TaskNotFoundException;
import com.example.Excermol.mapper.TagMapper;
import com.example.Excermol.repository.PersonRepository;
import com.example.Excermol.repository.TagRepository;
import com.example.Excermol.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@DisplayName("TagServiceImplTest Unit Tests")
public class TagServiceImplTest {
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private TaskRepository taskRepository;
    @Mock
    private TagMapper tagMapper;

    @InjectMocks
    private TagServiceImpl tagService;

    private Tag tag;
    private TagCreateRequestDTO createDTO;
    private TagUpdateRequestDTO updateDTO;
    private TagResponseDTO responseDTO;
    private Person person;
    private Task task;

    @BeforeEach
    void setUp() {
        tag = new Tag();
        tag.setId(1L);
        tag.setName("Agency");
        tag.setColor("#FF0000");
        tag.setPersons(new HashSet<>());
        tag.setTasks(new HashSet<>());

        createDTO = new TagCreateRequestDTO();
        createDTO.setName("Agency");
        createDTO.setColor("#FF0000");

        updateDTO = new TagUpdateRequestDTO();
        updateDTO.setName("Updated Agency");

        responseDTO = new TagResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Agency");

        person = new Person();
        person.setId(10L);
        person.setTags(new HashSet<>());

        task = new Task();
        task.setId(20L);
        task.setTags(new HashSet<>());
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createTag_shouldSaveAndReturnTag() {
        when(tagRepository.existsByName("Agency")).thenReturn(false);
        when(tagMapper.toEntity(createDTO)).thenReturn(tag);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        TagResponseDTO result = tagService.createTag(createDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Agency");
        verify(tagRepository).save(tag);
    }

    @Test
    void createTag_shouldThrowException_whenTagAlreadyExists() {
        when(tagRepository.existsByName("Agency")).thenReturn(true);

        assertThrows(TagAlreadyExistsException.class, () -> tagService.createTag(createDTO));
        verify(tagRepository, never()).save(any());
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAllTags_shouldReturnAllTags() {
        when(tagRepository.findAll()).thenReturn(List.of(tag));
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        List<TagResponseDTO> result = tagService.getAllTags();

        assertThat(result).hasSize(1);
        verify(tagRepository).findAll();
    }

    @Test
    void getAllTags_shouldReturnEmptyList_whenNoTags() {
        when(tagRepository.findAll()).thenReturn(List.of());

        List<TagResponseDTO> result = tagService.getAllTags();

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getTagById_shouldReturnTag_whenExists() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        TagResponseDTO result = tagService.getTagById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(tagRepository).findById(1L);
    }

    @Test
    void getTagById_shouldThrowException_whenNotFound() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.getTagById(99L));
    }

    // =========================
    // SEARCH BY NAME
    // =========================
    @Test
    void searchTagsByName_shouldReturnMatchingTags() {
        when(tagRepository.findAllByNameContainingIgnoreCase("agency")).thenReturn(List.of(tag));
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        List<TagResponseDTO> result = tagService.searchTagsByName("agency");

        assertThat(result).hasSize(1);
        verify(tagRepository).findAllByNameContainingIgnoreCase("agency");
    }

    @Test
    void searchTagsByName_shouldReturnEmptyList_whenNoMatches() {
        when(tagRepository.findAllByNameContainingIgnoreCase("xyz")).thenReturn(List.of());

        List<TagResponseDTO> result = tagService.searchTagsByName("xyz");

        assertThat(result).isEmpty();
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateTag_shouldUpdateAndReturnTag() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByName("Updated Agency")).thenReturn(false);
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        TagResponseDTO result = tagService.updateTag(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(tagMapper).updateEntity(tag, updateDTO);
        verify(tagRepository).save(tag);
    }

    @Test
    void updateTag_shouldThrowException_whenTagNotFound() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.updateTag(99L, updateDTO));
        verify(tagRepository, never()).save(any());
    }

    @Test
    void updateTag_shouldThrowException_whenNewNameAlreadyExists() {
        tag.setName("OldName");
        updateDTO.setName("ExistingTag");

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.existsByName("ExistingTag")).thenReturn(true);

        assertThrows(TagAlreadyExistsException.class, () -> tagService.updateTag(1L, updateDTO));
        verify(tagRepository, never()).save(any());
    }

    @Test
    void updateTag_shouldNotCheckDuplicate_whenNameUnchanged() {
        updateDTO.setName("Agency"); // eyni ad

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(tagRepository.save(tag)).thenReturn(tag);
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        tagService.updateTag(1L, updateDTO);

        verify(tagRepository, never()).existsByName(any());
        verify(tagRepository).save(tag);
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteTag_shouldDeleteTag_whenExists() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));

        tagService.deleteTag(1L);

        verify(tagRepository).delete(tag);
    }

    @Test
    void deleteTag_shouldThrowException_whenNotFound() {
        when(tagRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.deleteTag(99L));
        verify(tagRepository, never()).delete(any());
    }

    // =========================
    // PERSON - TAG
    // =========================
    @Test
    void addTagToPerson_shouldAddTagToPerson() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(personRepository.findById(10L)).thenReturn(Optional.of(person));

        tagService.addTagToPerson(1L, 10L);

        assertThat(person.getTags()).contains(tag);
        verify(personRepository).save(person);
    }

    @Test
    void addTagToPerson_shouldThrowException_whenTagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.addTagToPerson(1L, 10L));
        verify(personRepository, never()).save(any());
    }

    @Test
    void addTagToPerson_shouldThrowException_whenPersonNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(personRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> tagService.addTagToPerson(1L, 10L));
        verify(personRepository, never()).save(any());
    }

    @Test
    void addTagToPerson_shouldThrowException_whenTagAlreadyAddedToPerson() {
        person.getTags().add(tag);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(personRepository.findById(10L)).thenReturn(Optional.of(person));

        assertThrows(TagAlreadyExistsException.class, () -> tagService.addTagToPerson(1L, 10L));
        verify(personRepository, never()).save(any());
    }

    @Test
    void removeTagFromPerson_shouldRemoveTagFromPerson() {
        person.getTags().add(tag);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(personRepository.findById(10L)).thenReturn(Optional.of(person));

        tagService.removeTagFromPerson(1L, 10L);

        assertThat(person.getTags()).doesNotContain(tag);
        verify(personRepository).save(person);
    }

    @Test
    void removeTagFromPerson_shouldThrowException_whenTagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.removeTagFromPerson(1L, 10L));
    }

    @Test
    void removeTagFromPerson_shouldThrowException_whenPersonNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(personRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> tagService.removeTagFromPerson(1L, 10L));
    }

    @Test
    void getTagsByPersonId_shouldReturnTags() {
        person.getTags().add(tag);

        when(personRepository.findById(10L)).thenReturn(Optional.of(person));
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        List<TagResponseDTO> result = tagService.getTagsByPersonId(10L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getTagsByPersonId_shouldThrowException_whenPersonNotFound() {
        when(personRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> tagService.getTagsByPersonId(10L));
    }

    // =========================
    // TASK - TAG
    // =========================
    @Test
    void addTagToTask_shouldAddTagToTask() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(20L)).thenReturn(Optional.of(task));

        tagService.addTagToTask(1L, 20L);

        assertThat(task.getTags()).contains(tag);
        verify(taskRepository).save(task);
    }

    @Test
    void addTagToTask_shouldThrowException_whenTagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.addTagToTask(1L, 20L));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void addTagToTask_shouldThrowException_whenTaskNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> tagService.addTagToTask(1L, 20L));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void addTagToTask_shouldThrowException_whenTagAlreadyAddedToTask() {
        task.getTags().add(tag);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(20L)).thenReturn(Optional.of(task));

        assertThrows(TagAlreadyExistsException.class, () -> tagService.addTagToTask(1L, 20L));
        verify(taskRepository, never()).save(any());
    }

    @Test
    void removeTagFromTask_shouldRemoveTagFromTask() {
        task.getTags().add(tag);

        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(20L)).thenReturn(Optional.of(task));

        tagService.removeTagFromTask(1L, 20L);

        assertThat(task.getTags()).doesNotContain(tag);
        verify(taskRepository).save(task);
    }

    @Test
    void removeTagFromTask_shouldThrowException_whenTagNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TagNotFoundException.class, () -> tagService.removeTagFromTask(1L, 20L));
    }

    @Test
    void removeTagFromTask_shouldThrowException_whenTaskNotFound() {
        when(tagRepository.findById(1L)).thenReturn(Optional.of(tag));
        when(taskRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> tagService.removeTagFromTask(1L, 20L));
    }

    @Test
    void getTagsByTaskId_shouldReturnTags() {
        task.getTags().add(tag);

        when(taskRepository.findById(20L)).thenReturn(Optional.of(task));
        when(tagMapper.toResponseDTO(tag)).thenReturn(responseDTO);

        List<TagResponseDTO> result = tagService.getTagsByTaskId(20L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getTagsByTaskId_shouldThrowException_whenTaskNotFound() {
        when(taskRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> tagService.getTagsByTaskId(20L));
    }
}
