package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.TagService;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    private final PersonRepository personRepository;
    private final TaskRepository taskRepository;
    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository,
                          PersonRepository personRepository,
                          TaskRepository taskRepository,
                          TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.personRepository = personRepository;
        this.taskRepository = taskRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagResponseDTO createTag(TagCreateRequestDTO dto) {
        log.info("Creating tag with name: {}", dto.getName());
        if (tagRepository.existsByName(dto.getName())) {
            log.warn("Tag already exists with name: {}", dto.getName());
            throw new TagAlreadyExistsException("Bu adda tag artıq mövcuddur: " + dto.getName());
        }
        Tag tag = tagMapper.toEntity(dto);
        Tag saved = tagRepository.save(tag);
        log.info("Tag created successfully with id: {}", saved.getId());
        return tagMapper.toResponseDTO(saved);
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        log.info("Fetching all tags");
        List<TagResponseDTO> tags = tagRepository.findAll()
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} tags", tags.size());
        return tags;
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        log.info("Fetching tag with id: {}", id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tag not found with id: {}", id);
                    return new TagNotFoundException("Tag tapılmadı: " + id);
                });
        log.info("Tag found with id: {}", id);
        return tagMapper.toResponseDTO(tag);
    }

    @Override
    public List<TagResponseDTO> searchTagsByName(String name) {
        log.info("Searching tags with name: {}", name);
        List<TagResponseDTO> tags = tagRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} tags matching name: {}", tags.size(), name);
        return tags;
    }

    @Override
    public TagResponseDTO updateTag(Long id, TagUpdateRequestDTO dto) {
        log.info("Updating tag with id: {}", id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tag not found for update. Id: {}", id);
                    return new TagNotFoundException("Tag tapılmadı: " + id);
                });

        if (dto.getName() != null && !dto.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(dto.getName())) {
                log.warn("Tag already exists with name: {}", dto.getName());
                throw new TagAlreadyExistsException("Bu adda tag artıq mövcuddur: " + dto.getName());
            }
        }

        tagMapper.updateEntity(tag, dto);
        Tag updated = tagRepository.save(tag);
        log.info("Tag updated successfully. Id: {}", id);
        return tagMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteTag(Long id) {
        log.info("Deleting tag with id: {}", id);
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Tag not found for deletion. Id: {}", id);
                    return new TagNotFoundException("Tag tapılmadı: " + id);
                });
        tagRepository.delete(tag);
        log.info("Tag deleted successfully. Id: {}", id);
    }

    // =========================
    // PERSON - TAG
    // =========================

    @Override
    public void addTagToPerson(Long tagId, Long personId) {
        log.info("Adding tag id: {} to person id: {}", tagId, personId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> {
                    log.warn("Tag not found with id: {}", tagId);
                    return new TagNotFoundException("Tag tapılmadı: " + tagId);
                });
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> {
                    log.warn("Person not found with id: {}", personId);
                    return new PersonNotFoundException("Person tapılmadı: " + personId);
                });


        if (person.getTags().contains(tag)) {
            log.warn("Tag id: {} already added to person id: {}", tagId, personId);
            throw new TagAlreadyExistsException("Bu tag artıq person-a əlavə edilib");
        }

        person.getTags().add(tag);
        personRepository.save(person);
        log.info("Tag id: {} added to person id: {} successfully", tagId, personId);
    }

    @Override
    public void removeTagFromPerson(Long tagId, Long personId) {
        log.info("Removing tag id: {} from person id: {}", tagId, personId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> {
                    log.warn("Tag not found with id: {}", tagId);
                    return new TagNotFoundException("Tag tapılmadı: " + tagId);
                });

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> {
                    log.warn("Person not found with id: {}", personId);
                    return new PersonNotFoundException("Person tapılmadı: " + personId);
                });
        person.getTags().remove(tag);
        personRepository.save(person);
        log.info("Tag id: {} removed from person id: {} successfully", tagId, personId);
    }

    @Override
    public List<TagResponseDTO> getTagsByPersonId(Long personId) {
        log.info("Fetching tags for person id: {}", personId);
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> {
                    log.warn("Person not found with id: {}", personId);
                    return new PersonNotFoundException("Person tapılmadı: " + personId);
                });

        List<TagResponseDTO> tags = person.getTags()
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} tags for person id: {}", tags.size(), personId);
        return tags;
    }

    // =========================
    // TASK - TAG
    // =========================

    @Override
    public void addTagToTask(Long tagId, Long taskId) {
        log.info("Adding tag id: {} to task id: {}", tagId, taskId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> {
                    log.warn("Tag not found with id: {}", tagId);
                    return new TagNotFoundException("Tag tapılmadı: " + tagId);
                });

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task not found with id: {}", taskId);
                    return new TaskNotFoundException("Task tapılmadı: " + taskId);
                });


        if (task.getTags().contains(tag)) {
            log.warn("Tag id: {} already added to task id: {}", tagId, taskId);
            throw new TagAlreadyExistsException("Bu tag artıq task-a əlavə edilib");
        }

        task.getTags().add(tag);
        taskRepository.save(task);
        log.info("Tag id: {} added to task id: {} successfully", tagId, taskId);
    }

    @Override
    public void removeTagFromTask(Long tagId, Long taskId) {
        log.info("Removing tag id: {} from task id: {}", tagId, taskId);
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> {
                    log.warn("Tag not found with id: {}", tagId);
                    return new TagNotFoundException("Tag tapılmadı: " + tagId);
                });
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task not found with id: {}", taskId);
                    return new TaskNotFoundException("Task tapılmadı: " + taskId);
                });

        task.getTags().remove(tag);
        taskRepository.save(task);
        log.info("Tag id: {} removed from task id: {} successfully", tagId, taskId);
    }

    @Override
    public List<TagResponseDTO> getTagsByTaskId(Long taskId) {
        log.info("Fetching tags for task id: {}", taskId);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task not found with id: {}", taskId);
                    return new TaskNotFoundException("Task tapılmadı: " + taskId);
                });

        List<TagResponseDTO> tags =  task.getTags()
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} tags for task id: {}", tags.size(), taskId);
        return tags;
    }
}