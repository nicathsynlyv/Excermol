package com.example.Excermol.Service;

import com.example.Excermol.entity.Tag;
import com.example.Excermol.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    public Tag createTag(Tag tag) {
        return tagRepository.save(tag);
    }
}
