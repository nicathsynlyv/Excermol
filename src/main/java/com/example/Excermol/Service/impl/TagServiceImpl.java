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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
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
        if (tagRepository.existsByName(dto.getName())) {
            throw new TagAlreadyExistsException("Bu adda tag artıq mövcuddur: " + dto.getName());
        }
        Tag tag = tagMapper.toEntity(dto);
        Tag saved = tagRepository.save(tag);
        return tagMapper.toResponseDTO(saved);
    }

    @Override
    public List<TagResponseDTO> getAllTags() {
        return tagRepository.findAll()
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDTO getTagById(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + id));
        return tagMapper.toResponseDTO(tag);
    }

    @Override
    public List<TagResponseDTO> searchTagsByName(String name) {
        return tagRepository.findAllByNameContainingIgnoreCase(name)
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagResponseDTO updateTag(Long id, TagUpdateRequestDTO dto) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + id));

        if (dto.getName() != null && !dto.getName().equals(tag.getName())) {
            if (tagRepository.existsByName(dto.getName())) {
                throw new TagAlreadyExistsException("Bu adda tag artıq mövcuddur: " + dto.getName());
            }
        }

        tagMapper.updateEntity(tag, dto);
        Tag updated = tagRepository.save(tag);
        return tagMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteTag(Long id) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + id));
        tagRepository.delete(tag);
    }

    // =========================
    // PERSON - TAG
    // =========================

    @Override
    public void addTagToPerson(Long tagId, Long personId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + tagId));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Person tapılmadı: " + personId));

        if (person.getTags().contains(tag)) {
            throw new TagAlreadyExistsException("Bu tag artıq person-a əlavə edilib");
        }

        person.getTags().add(tag);
        personRepository.save(person);
    }

    @Override
    public void removeTagFromPerson(Long tagId, Long personId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + tagId));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Person tapılmadı: " + personId));

        person.getTags().remove(tag);
        personRepository.save(person);
    }

    @Override
    public List<TagResponseDTO> getTagsByPersonId(Long personId) {
        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Person tapılmadı: " + personId));

        return person.getTags()
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // =========================
    // TASK - TAG
    // =========================

    @Override
    public void addTagToTask(Long tagId, Long taskId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + tagId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: " + taskId));

        if (task.getTags().contains(tag)) {
            throw new TagAlreadyExistsException("Bu tag artıq task-a əlavə edilib");
        }

        task.getTags().add(tag);
        taskRepository.save(task);
    }

    @Override
    public void removeTagFromTask(Long tagId, Long taskId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + tagId));

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: " + taskId));

        task.getTags().remove(tag);
        taskRepository.save(task);
    }

    @Override
    public List<TagResponseDTO> getTagsByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: " + taskId));

        return task.getTags()
                .stream()
                .map(tagMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}