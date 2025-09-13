package com.example.Excermol.controller;


import com.example.Excermol.Service.impl.BuilderServiceImpl;
import com.example.Excermol.entity.Builder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/builders")
@Tag(name = "Builder API", description = "Builderlər üçün əməliyyatlar")
public class BuilderController {

    private final BuilderServiceImpl builderServiceImpl;

    @Autowired
    public BuilderController(BuilderServiceImpl builderServiceImpl) {
        this.builderServiceImpl = builderServiceImpl;
    }

    @Operation(summary = "Bütün builderləri gətir", description = "Sistemdəki bütün builderlərin siyahısını qaytarır")
    @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    @GetMapping
    public ResponseEntity<List<Builder>> getAllBuilders() {
        return ResponseEntity.ok(builderServiceImpl.getAll());
    }

    @Operation(summary = "ID ilə builder tap", description = "Verilmiş ID-yə uyğun builderi qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Builder tapıldı"),
            @ApiResponse(responseCode = "404", description = "Builder tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Builder> getBuilderById(@PathVariable Long id) {
        return builderServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni builder əlavə et", description = "Yeni builder yaradır və saxlayır")
    @ApiResponse(responseCode = "200", description = "Builder uğurla yaradıldı")
    @PostMapping
    public ResponseEntity<Builder> createBuilder(@RequestBody Builder builder) {
        return ResponseEntity.ok(builderServiceImpl.save(builder));
    }

    @Operation(summary = "Builderi yenilə", description = "Mövcud builderin məlumatlarını dəyişir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Builder uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Builder tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Builder> updateBuilder(@PathVariable Long id, @RequestBody Builder builder) {
        return builderServiceImpl.getById(id)
                .map(existingBuilder -> {
                    builder.setId(id);
                    return ResponseEntity.ok(builderServiceImpl.save(builder));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Builderi sil", description = "Verilmiş ID-yə uyğun builderi silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Builder uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Builder tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilder(@PathVariable Long id) {
        if (builderServiceImpl.getById(id).isPresent()) {
            builderServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Form ID-yə görə builder tap", description = "Verilmiş form ID-yə uyğun builderi qaytarır")
    @ApiResponse(responseCode = "200", description = "Builder tapıldı")
    @GetMapping("/by-form-id/{formId}")
    public ResponseEntity<Builder> getBuilderByFormId(@PathVariable Long formId) {
        return builderServiceImpl.findByFormId(formId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Form adına görə builder tap", description = "Verilmiş form adına uyğun builderi qaytarır")
    @ApiResponse(responseCode = "200", description = "Builder tapıldı")
    @GetMapping("/by-form-title")
    public ResponseEntity<Builder> getBuilderByFormTitle(@RequestParam String title) {
        return builderServiceImpl.findByFormTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
