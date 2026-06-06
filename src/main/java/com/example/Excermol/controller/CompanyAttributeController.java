package com.example.Excermol.controller;

import com.example.Excermol.Service.CompanyAttributeService;
import com.example.Excermol.entity.dtos.CompanyAttributeCreateRequestDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeResponseDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeUpdateRequestDTO;
import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/company-attributes")
@Tag(name = "CompanyAttribute API", description = "Settings -> Company tab əməliyyatları")
public class CompanyAttributeController {

    private final CompanyAttributeService companyAttributeService;

    public CompanyAttributeController(CompanyAttributeService companyAttributeService) {
        this.companyAttributeService = companyAttributeService;
    }

    @Operation(summary = "Yeni attribute yarat - 'Create attribute'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribute uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "Workspace tapılmadı")
    })
    @PostMapping
    public ResponseEntity<CompanyAttributeResponseDTO> createAttribute(
            @RequestBody CompanyAttributeCreateRequestDTO dto) {
        return ResponseEntity.ok(companyAttributeService.createAttribute(dto));
    }

    @Operation(summary = "Workspace-ə görə bütün attribute-lar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<CompanyAttributeResponseDTO>> getAttributesByWorkspaceId(
            @PathVariable Long workspaceId) {
        return ResponseEntity.ok(companyAttributeService.getAttributesByWorkspaceId(workspaceId));
    }

    @Operation(summary = "ID ilə attribute tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribute tapıldı"),
            @ApiResponse(responseCode = "404", description = "Attribute tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyAttributeResponseDTO> getAttributeById(@PathVariable Long id) {
        return ResponseEntity.ok(companyAttributeService.getAttributeById(id));
    }

    @Operation(summary = "Property-ə görə attribute-lar - SYSTEM və ya CUSTOM")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}/property/{property}")
    public ResponseEntity<List<CompanyAttributeResponseDTO>> getAttributesByProperty(
            @PathVariable Long workspaceId,
            @PathVariable AttributeProperty property) {
        return ResponseEntity.ok(companyAttributeService.getAttributesByProperty(workspaceId, property));
    }

    @Operation(summary = "Tipə görə attribute-lar - EMAIL, TEXT, LINKEDIN...")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}/type/{type}")
    public ResponseEntity<List<CompanyAttributeResponseDTO>> getAttributesByType(
            @PathVariable Long workspaceId,
            @PathVariable AttributeType type) {
        return ResponseEntity.ok(companyAttributeService.getAttributesByType(workspaceId, type));
    }

    @Operation(summary = "Ada görə axtarış - 'Search by name'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}/search")
    public ResponseEntity<List<CompanyAttributeResponseDTO>> searchAttributesByName(
            @PathVariable Long workspaceId,
            @RequestParam String name) {
        return ResponseEntity.ok(companyAttributeService.searchAttributesByName(workspaceId, name));
    }

    @Operation(summary = "Attribute update et - yalnız CUSTOM olanlar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Attribute uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Attribute tapılmadı"),
            @ApiResponse(responseCode = "400", description = "SYSTEM attribute dəyişdirilə bilməz")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyAttributeResponseDTO> updateAttribute(
            @PathVariable Long id,
            @RequestBody CompanyAttributeUpdateRequestDTO dto) {
        return ResponseEntity.ok(companyAttributeService.updateAttribute(id, dto));
    }

    @Operation(summary = "Attribute sil - yalnız CUSTOM olanlar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Attribute uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Attribute tapılmadı"),
            @ApiResponse(responseCode = "400", description = "SYSTEM attribute silinə bilməz")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttribute(@PathVariable Long id) {
        companyAttributeService.deleteAttribute(id);
        return ResponseEntity.noContent().build();
    }
}
