package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.TagServiceImpl;
import com.example.Excermol.entity.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Controller", description = "Tag-larla əlaqəli bütün əməliyyatlar")
public class TagController {

    private final TagServiceImpl tagServiceImpl;

    public TagController(TagServiceImpl tagServiceImpl) {
        this.tagServiceImpl = tagServiceImpl;
    }

    @Operation(summary = "Bütün tag-ları gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Bütün tag-lar uğurla gətirildi")
    })
    @GetMapping
    public ResponseEntity<List<Tag>> getAllTags() {
        return ResponseEntity.ok(tagServiceImpl.getAll());
    }

    @Operation(summary = "Tag-ı ID-ə görə gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag tapıldı"),
            @ApiResponse(responseCode = "404", description = "Tag tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Tag> getTagById(@PathVariable Long id) {
        return tagServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Adı ilə tag-ı gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag tapıldı"),
            @ApiResponse(responseCode = "404", description = "Tag tapılmadı")
    })
    @GetMapping("/name/{name}")
    public ResponseEntity<Tag> getTagByName(@PathVariable String name) {
        Tag tag = tagServiceImpl.findByName(name);
        if (tag != null) {
            return ResponseEntity.ok(tag);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Yeni tag yaradır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tag uğurla yaradıldı")
    })
    @PostMapping
    public ResponseEntity<Tag> createTag(@RequestBody Tag tag) {
        Tag savedTag = tagServiceImpl.save(tag);
        return ResponseEntity.status(201).body(savedTag);
    }

    @Operation(summary = "Tag-ı ID-ə görə update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag uğurla update olundu"),
            @ApiResponse(responseCode = "404", description = "Tag tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        return tagServiceImpl.getById(id)
                .map(existingTag -> {
                    existingTag.setName(tag.getName());
                    existingTag.setColor(tag.getColor());
                    Tag updatedTag = tagServiceImpl.save(existingTag);
                    return ResponseEntity.ok(updatedTag);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Tag-ı ID-ə görə silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Tag tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        if (tagServiceImpl.getById(id).isPresent()) {
            tagServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
