package com.example.Excermol.controller;

import com.example.Excermol.Service.FormRoutingService;
import com.example.Excermol.entity.dtos.FormRoutingCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormRoutingResponseDTO;
import com.example.Excermol.entity.dtos.FormRoutingUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/form-routings")
@Tag(name = "FormRouting API", description = "Form routing-ləri üçün əməliyyatlar")
public class FormRoutingController {

    private final FormRoutingService formRoutingService;

    public FormRoutingController(FormRoutingService formRoutingService) {
        this.formRoutingService = formRoutingService;
    }

    @Operation(summary = "Yeni routing yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "Form və ya FormField tapılmadı")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping
    public ResponseEntity<FormRoutingResponseDTO> createRouting(
            @Valid @RequestBody FormRoutingCreateRequestDTO dto) {
        return ResponseEntity.ok(formRoutingService.createRouting(dto));
    }

    @Operation(summary = "Forma görə bütün routing-ləri gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/form/{formId}")
    public ResponseEntity<List<FormRoutingResponseDTO>> getRoutingsByFormId(
            @PathVariable Long formId) {
        return ResponseEntity.ok(formRoutingService.getRoutingsByFormId(formId));
    }

    @Operation(summary = "ID ilə routing tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing tapıldı"),
            @ApiResponse(responseCode = "404", description = "Routing tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<FormRoutingResponseDTO> getRoutingById(@PathVariable Long id) {
        return ResponseEntity.ok(formRoutingService.getRoutingById(id));
    }

    @Operation(summary = "Routing-i update et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Routing tapılmadı")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<FormRoutingResponseDTO> updateRouting(
            @PathVariable Long id,
            @Valid @RequestBody FormRoutingUpdateRequestDTO dto) {
        return ResponseEntity.ok(formRoutingService.updateRouting(id, dto));
    }

    @Operation(summary = "Routing-i sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Routing uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Routing tapılmadı")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRouting(@PathVariable Long id) {
        formRoutingService.deleteRouting(id);
        return ResponseEntity.noContent().build();
    }
}