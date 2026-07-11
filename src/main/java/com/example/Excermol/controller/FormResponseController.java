package com.example.Excermol.controller;


import com.example.Excermol.Service.FormResponseService;
import com.example.Excermol.entity.dtos.FormSubmitRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form-responses")
@Tag(name = "FormResponse API", description = "Form cavabları üçün əməliyyatlar")
public class FormResponseController {

    private final FormResponseService formResponseService;

    public FormResponseController(FormResponseService formResponseService) {
        this.formResponseService = formResponseService;
    }

    @Operation(summary = "Formu submit et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Form uğurla submit edildi"),
            @ApiResponse(responseCode = "404", description = "Form tapılmadı")
    })
    @PostMapping("/submit")
    public ResponseEntity<FormSubmitResponseDTO> submitForm(
            @Valid @RequestBody FormSubmitRequestDTO dto) {
        return ResponseEntity.ok(formResponseService.submitForm(dto));
    }

    @Operation(summary = "Forma görə bütün cavabları gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/form/{formId}")
    public ResponseEntity<List<FormSubmitResponseDTO>> getResponsesByFormId(
            @PathVariable Long formId) {
        return ResponseEntity.ok(formResponseService.getResponsesByFormId(formId));
    }

    @Operation(summary = "ID ilə cavab tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cavab tapıldı"),
            @ApiResponse(responseCode = "404", description = "Cavab tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FormSubmitResponseDTO> getResponseById(@PathVariable Long id) {
        return ResponseEntity.ok(formResponseService.getResponseById(id));
    }

    @Operation(summary = "Cavabı sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cavab uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Cavab tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long id) {
        formResponseService.deleteResponse(id);
        return ResponseEntity.noContent().build();
    }
}