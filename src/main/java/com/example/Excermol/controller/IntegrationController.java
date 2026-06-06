package com.example.Excermol.controller;

import com.example.Excermol.Service.IntegrationService;
import com.example.Excermol.entity.dtos.IntegrationCreateRequestDTO;
import com.example.Excermol.entity.dtos.IntegrationResponseDTO;
import com.example.Excermol.entity.dtos.IntegrationUpdateRequestDTO;
import com.example.Excermol.enums.IntegrationType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/integrations")
@Tag(name = "Integration API", description = "Settings -> Integrations tab əməliyyatları")
public class IntegrationController {

    private final IntegrationService integrationService;

    public IntegrationController(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }
//1
    @Operation(summary = "Yeni integration yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Integration uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "Workspace tapılmadı"),
            @ApiResponse(responseCode = "400", description = "Bu tip integration artıq mövcuddur")
    })
    @PostMapping
    public ResponseEntity<IntegrationResponseDTO> createIntegration(
            @RequestBody IntegrationCreateRequestDTO dto) {
        return ResponseEntity.ok(integrationService.createIntegration(dto));
    }
//2
    @Operation(summary = "Workspace-ə görə bütün integration-lar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<IntegrationResponseDTO>> getIntegrationsByWorkspaceId(
            @PathVariable Long workspaceId) {
        return ResponseEntity.ok(integrationService.getIntegrationsByWorkspaceId(workspaceId));
    }
//3
    @Operation(summary = "ID ilə integration tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Integration tapıldı"),
            @ApiResponse(responseCode = "404", description = "Integration tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<IntegrationResponseDTO> getIntegrationById(@PathVariable Long id) {
        return ResponseEntity.ok(integrationService.getIntegrationById(id));
    }
//4
    @Operation(summary = "Workspace və tipə görə integration tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Integration tapıldı"),
            @ApiResponse(responseCode = "404", description = "Integration tapılmadı")
    })
    @GetMapping("/workspace/{workspaceId}/type/{type}")
    public ResponseEntity<IntegrationResponseDTO> getIntegrationByType(
            @PathVariable Long workspaceId,
            @PathVariable IntegrationType type) {
        return ResponseEntity.ok(integrationService.getIntegrationByType(workspaceId, type));
    }
//5
    @Operation(summary = "Aktiv integration-lar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}/active")
    public ResponseEntity<List<IntegrationResponseDTO>> getActiveIntegrations(
            @PathVariable Long workspaceId) {
        return ResponseEntity.ok(integrationService.getActiveIntegrations(workspaceId));
    }
//6
    @Operation(summary = "Integration update et - description, toggle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Integration uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Integration tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<IntegrationResponseDTO> updateIntegration(
            @PathVariable Long id,
            @RequestBody IntegrationUpdateRequestDTO dto) {
        return ResponseEntity.ok(integrationService.updateIntegration(id, dto));
    }
//7
    @Operation(summary = "Integration toggle et - aktiv/deaktiv")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Toggle uğurla edildi"),
            @ApiResponse(responseCode = "404", description = "Integration tapılmadı")
    })
    @PatchMapping("/{id}/toggle")
    public ResponseEntity<IntegrationResponseDTO> toggleIntegration(@PathVariable Long id) {
        return ResponseEntity.ok(integrationService.toggleIntegration(id));
    }
//8
    @Operation(summary = "Integration sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Integration uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Integration tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIntegration(@PathVariable Long id) {
        integrationService.deleteIntegration(id);
        return ResponseEntity.noContent().build();
    }
}