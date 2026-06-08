package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.TagService;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.Tag;
import com.example.Excermol.entity.dtos.TagCreateRequestDTO;
import com.example.Excermol.entity.dtos.TagResponseDTO;
import com.example.Excermol.entity.dtos.TagUpdateRequestDTO;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.exception.TagAlreadyExistsException;
import com.example.Excermol.exception.TagNotFoundException;
import com.example.Excermol.mapper.TagMapper;
import com.example.Excermol.repository.PersonRepository;
import com.example.Excermol.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;
    private final PersonRepository personRepository;
    private final TagMapper tagMapper;

    public TagServiceImpl(TagRepository tagRepository,
                          PersonRepository personRepository,
                          TagMapper tagMapper) {
        this.tagRepository = tagRepository;
        this.personRepository = personRepository;
        this.tagMapper = tagMapper;
    }

    @Override
    public TagResponseDTO createTag(TagCreateRequestDTO dto) {
        // eyni adlı tag artıq varmı?
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

        // ad dəyişirsə eyni adlı tag varmı yoxla
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

    @Override
    public void addTagToPerson(Long tagId, Long personId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new TagNotFoundException("Tag tapılmadı: " + tagId));

        Person person = personRepository.findById(personId)
                .orElseThrow(() -> new PersonNotFoundException("Person tapılmadı: " + personId));

        // tag artıq person-dadırsa əlavə etmə
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
}