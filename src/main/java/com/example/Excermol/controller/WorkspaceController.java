package com.example.Excermol.controller;

import com.example.Excermol.Service.WorkspaceService;
import com.example.Excermol.entity.dtos.WorkspaceCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspaces")
@Tag(name = "Workspace API", description = "Settings -> General tab əməliyyatları")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @Operation(summary = "Yeni workspace yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "User tapılmadı")
    })
    @PostMapping
    public ResponseEntity<WorkspaceResponseDTO> createWorkspace(
            @RequestBody WorkspaceCreateRequestDTO dto) {
        return ResponseEntity.ok(workspaceService.createWorkspace(dto));
    }

    @Operation(summary = "Bütün workspace-ləri gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping
    public ResponseEntity<List<WorkspaceResponseDTO>> getAllWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @Operation(summary = "ID ilə workspace tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace tapıldı"),
            @ApiResponse(responseCode = "404", description = "Workspace tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDTO> getWorkspaceById(@PathVariable Long id) {
        return ResponseEntity.ok(workspaceService.getWorkspaceById(id));
    }

    @Operation(summary = "Owner-a görə workspace-lər")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<WorkspaceResponseDTO>> getWorkspacesByOwner(
            @PathVariable Long ownerId) {
        return ResponseEntity.ok(workspaceService.getWorkspacesByOwner(ownerId));
    }

    @Operation(summary = "Workspace update et - General tab 'Update workspace'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workspace uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Workspace tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WorkspaceResponseDTO> updateWorkspace(
            @PathVariable Long id,
            @RequestBody WorkspaceUpdateRequestDTO dto) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(id, dto));
    }

    @Operation(summary = "Workspace sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Workspace uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Workspace tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorkspace(@PathVariable Long id) {
        workspaceService.deleteWorkspace(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Workspace-i reset et - General tab 'Reset workspace'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Workspace uğurla reset edildi"),
            @ApiResponse(responseCode = "404", description = "Workspace tapılmadı")
    })
    @DeleteMapping("/{id}/reset")
    public ResponseEntity<Void> resetWorkspace(@PathVariable Long id) {
        workspaceService.resetWorkspace(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Workspace-dən çıx - General tab 'Leave workspace'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Workspace-dən uğurla çıxıldı"),
            @ApiResponse(responseCode = "404", description = "Workspace tapılmadı")
    })
    @DeleteMapping("/{workspaceId}/leave/{userId}")
    public ResponseEntity<Void> leaveWorkspace(
            @PathVariable Long workspaceId,
            @PathVariable Long userId) {
        workspaceService.leaveWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }
}
