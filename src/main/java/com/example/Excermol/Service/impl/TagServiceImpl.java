package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.TagService;
import com.example.Excermol.entity.Tag;
import com.example.Excermol.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // ===== BaseService CRUD əməliyyatları =====
    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll();
    }

    @Override
    public Optional<Tag> getById(Long id) {
        return tagRepository.findById(id);
    }

    @Override
    public Tag save(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public void deleteById(Long id) {
        tagRepository.deleteById(id);
    }

    // ===== TagService xüsusi metodları =====
    @Override
    public Tag findByName(String name) {
        return tagRepository.findByName(name);
    }
}
