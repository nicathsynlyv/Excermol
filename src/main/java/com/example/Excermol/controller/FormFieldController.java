package com.example.Excermol.controller;


import com.example.Excermol.Service.FormFieldService;
import com.example.Excermol.entity.dtos.FormFieldCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormFieldResponseDTO;
import com.example.Excermol.entity.dtos.FormFieldUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form-fields")
@Tag(name = "FormField API", description = "Form field-ləri üçün əməliyyatlar")
public class FormFieldController {

    private final FormFieldService formFieldService;

    public FormFieldController(FormFieldService formFieldService) {
        this.formFieldService = formFieldService;
    }

    @Operation(summary = "Yeni field yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "Form tapılmadı")
    })
    @PostMapping
    public ResponseEntity<FormFieldResponseDTO> createField(
            @Valid @RequestBody FormFieldCreateRequestDTO dto) {
        return ResponseEntity.ok(formFieldService.createField(dto));
    }

    @Operation(summary = "Forma görə bütün field-ləri gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/form/{formId}")
    public ResponseEntity<List<FormFieldResponseDTO>> getFieldsByFormId(
            @PathVariable Long formId) {
        return ResponseEntity.ok(formFieldService.getFieldsByFormId(formId));
    }

    @Operation(summary = "ID ilə field tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field tapıldı"),
            @ApiResponse(responseCode = "404", description = "Field tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FormFieldResponseDTO> getFieldById(@PathVariable Long id) {
        return ResponseEntity.ok(formFieldService.getFieldById(id));
    }

    @Operation(summary = "Field-i update et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Field uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Field tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<FormFieldResponseDTO> updateField(
            @PathVariable Long id,
            @Valid @RequestBody FormFieldUpdateRequestDTO dto) {
        return ResponseEntity.ok(formFieldService.updateField(id, dto));
    }

    @Operation(summary = "Field-i sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Field uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Field tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        formFieldService.deleteField(id);
        return ResponseEntity.noContent().build();
    }
}