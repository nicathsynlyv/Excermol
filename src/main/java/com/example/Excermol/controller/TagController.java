package com.example.Excermol.controller;

import com.example.Excermol.Service.TagService;

import com.example.Excermol.entity.dtos.TagCreateRequestDTO;
import com.example.Excermol.entity.dtos.TagResponseDTO;
import com.example.Excermol.entity.dtos.TagUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@Tag(name = "Tag API", description = "People → Tags əməliyyatları")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Operation(summary = "Yeni tag yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "Bu adda tag artıq mövcuddur")
    })
    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag(
            @RequestBody TagCreateRequestDTO dto) {
        return ResponseEntity.ok(tagService.createTag(dto));
    }

    @Operation(summary = "Bütün tag-ləri gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @Operation(summary = "ID ilə tag tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag tapıldı"),
            @ApiResponse(responseCode = "404", description = "Tag tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.getTagById(id));
    }

    @Operation(summary = "Ada görə tag axtarış")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/search")
    public ResponseEntity<List<TagResponseDTO>> searchTagsByName(
            @RequestParam String name) {
        return ResponseEntity.ok(tagService.searchTagsByName(name));
    }

    @Operation(summary = "Tag update et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tag uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Tag tapılmadı"),
            @ApiResponse(responseCode = "400", description = "Bu adda tag artıq mövcuddur")
    })
    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable Long id,
            @RequestBody TagUpdateRequestDTO dto) {
        return ResponseEntity.ok(tagService.updateTag(id, dto));
    }

    @Operation(summary = "Tag sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Tag tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Person-a tag əlavə et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag uğurla əlavə edildi"),
            @ApiResponse(responseCode = "404", description = "Tag və ya Person tapılmadı"),
            @ApiResponse(responseCode = "400", description = "Tag artıq person-a əlavə edilib")
    })
    @PostMapping("/{tagId}/person/{personId}")
    public ResponseEntity<Void> addTagToPerson(
            @PathVariable Long tagId,
            @PathVariable Long personId) {
        tagService.addTagToPerson(tagId, personId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Person-dan tag sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tag uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Tag və ya Person tapılmadı")
    })
    @DeleteMapping("/{tagId}/person/{personId}")
    public ResponseEntity<Void> removeTagFromPerson(
            @PathVariable Long tagId,
            @PathVariable Long personId) {
        tagService.removeTagFromPerson(tagId, personId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Person-un bütün tag-ləri")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat"),
            @ApiResponse(responseCode = "404", description = "Person tapılmadı")
    })
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<TagResponseDTO>> getTagsByPersonId(
            @PathVariable Long personId) {
        return ResponseEntity.ok(tagService.getTagsByPersonId(personId));
    }
}