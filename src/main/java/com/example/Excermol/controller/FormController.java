package com.example.Excermol.controller;



import com.example.Excermol.Service.FormService;
import com.example.Excermol.entity.dtos.FormCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormResponseDTO;
import com.example.Excermol.entity.dtos.FormUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@Tag(name = "Form API", description = "Formlar üçün əməliyyatlar")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @Operation(summary = "Yeni form yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Form uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "User tapılmadı")
    })
    @PostMapping
    public ResponseEntity<FormResponseDTO> createForm(
            @Valid @RequestBody FormCreateRequestDTO dto,
            @RequestParam Long ownerId) {
        return ResponseEntity.ok(formService.createForm(dto, ownerId));
    }

    @Operation(summary = "Bütün formları gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping
    public ResponseEntity<List<FormResponseDTO>> getAllForms() {
        return ResponseEntity.ok(formService.getAllForms());
    }

    @Operation(summary = "ID ilə form tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Form tapıldı"),
            @ApiResponse(responseCode = "404", description = "Form tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FormResponseDTO> getFormById(@PathVariable Long id) {
        return ResponseEntity.ok(formService.getFormById(id));
    }

    @Operation(summary = "Owner-a görə formları gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<FormResponseDTO>> getFormsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(formService.getFormsByOwner(ownerId));
    }

    @Operation(summary = "Formu update et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Form uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Form tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FormResponseDTO> updateForm(
            @PathVariable Long id,
           @Valid @RequestBody FormUpdateRequestDTO dto) {
        return ResponseEntity.ok(formService.updateForm(id, dto));
    }

    @Operation(summary = "Formu sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Form uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Form tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        formService.deleteForm(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Formu publish et / draft-a qaytar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status dəyişdirildi"),
            @ApiResponse(responseCode = "404", description = "Form tapılmadı")
    })
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<FormResponseDTO> toggleFormStatus(@PathVariable Long id) {
        return ResponseEntity.ok(formService.toggleFormStatus(id));
    }
}