package com.example.Excermol.controller;

import com.example.Excermol.Service.TagService;
import com.example.Excermol.entity.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {
    private final TagService tagService;
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/name/{name}")
    public Tag getTagByName(@PathVariable String name) {
        return tagService.getTagByName(name);
    }

    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }
}
