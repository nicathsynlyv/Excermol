package com.example.Excermol.controller;

import com.example.Excermol.Service.OrganizationService;
import com.example.Excermol.entity.dtos.OrganizationRequestDTO;
import com.example.Excermol.entity.dtos.OrganizationResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@Tag(name = "Organization Controller", description = "Organization CRUD əməliyyatları"
)
public class OrganizationController {

   private final OrganizationService organizationService;

    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }


    @Operation(summary = "Yeni organization yaratmaq")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organization uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = OrganizationResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping
    public ResponseEntity<OrganizationResponseDTO> createOrganization(
            @Valid @RequestBody OrganizationRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(organizationService.createOrganization(requestDTO));
    }

    @Operation(summary = "Bütün organization-ları gətir")
    @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı",
            content = @Content(schema = @Schema(implementation = OrganizationResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<OrganizationResponseDTO>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    @Operation(summary = "ID ilə organization gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization tapıldı",
                    content = @Content(schema = @Schema(implementation = OrganizationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> getOrganizationById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    @Operation(summary = "Organization yeniləmək")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization uğurla yeniləndi",
                    content = @Content(schema = @Schema(implementation = OrganizationResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Organization tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationResponseDTO> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequestDTO requestDTO) {
        return ResponseEntity.ok(organizationService.updateOrganization(id, requestDTO));
    }

    @Operation(summary = "Organization silmək")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Organization tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }

}
