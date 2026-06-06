package com.example.Excermol.controller;


import com.example.Excermol.Service.WorkspaceMemberService;
import com.example.Excermol.entity.dtos.WorkspaceMemberCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberUpdateRequestDTO;
import com.example.Excermol.enums.MemberRole;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workspace-members")
@Tag(name = "WorkspaceMember API", description = "Settings -> Members tab əməliyyatları")
public class WorkspaceMemberController {

    private final WorkspaceMemberService workspaceMemberService;

    public WorkspaceMemberController(WorkspaceMemberService workspaceMemberService) {
        this.workspaceMemberService = workspaceMemberService;
    }
//1
    @Operation(summary = "Yeni üzv əlavə et - 'Invite team member'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Üzv uğurla əlavə edildi"),
            @ApiResponse(responseCode = "404", description = "Workspace və ya User tapılmadı"),
            @ApiResponse(responseCode = "400", description = "User artıq bu workspace-dədir")
    })
    @PostMapping
    public ResponseEntity<WorkspaceMemberResponseDTO> addMember(
            @RequestBody WorkspaceMemberCreateRequestDTO dto) {
        return ResponseEntity.ok(workspaceMemberService.addMember(dto));
    }
//2
    @Operation(summary = "Workspace-ə görə bütün üzvlər")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<WorkspaceMemberResponseDTO>> getMembersByWorkspaceId(
            @PathVariable Long workspaceId) {
        return ResponseEntity.ok(workspaceMemberService.getMembersByWorkspaceId(workspaceId));
    }
//3
    @Operation(summary = "ID ilə üzv tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Üzv tapıldı"),
            @ApiResponse(responseCode = "404", description = "Üzv tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WorkspaceMemberResponseDTO> getMemberById(@PathVariable Long id) {
        return ResponseEntity.ok(workspaceMemberService.getMemberById(id));
    }
//4
    @Operation(summary = "Üzvün rolunu dəyiş - ADMIN, MEMBER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rol uğurla dəyişdirildi"),
            @ApiResponse(responseCode = "404", description = "Üzv tapılmadı"),
            @ApiResponse(responseCode = "400", description = "Owner-in rolu dəyişdirilə bilməz")
    })
    @PutMapping("/{id}/role")
    public ResponseEntity<WorkspaceMemberResponseDTO> updateMemberRole(
            @PathVariable Long id,
            @RequestBody WorkspaceMemberUpdateRequestDTO dto) {
        return ResponseEntity.ok(workspaceMemberService.updateMemberRole(id, dto));
    }
//5
    @Operation(summary = "Üzvü sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Üzv uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Üzv tapılmadı"),
            @ApiResponse(responseCode = "400", description = "Owner silinə bilməz")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeMember(@PathVariable Long id) {
        workspaceMemberService.removeMember(id);
        return ResponseEntity.noContent().build();
    }
//6
    @Operation(summary = "Rola görə üzvlər - OWNER, ADMIN, MEMBER")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}/role/{role}")
    public ResponseEntity<List<WorkspaceMemberResponseDTO>> getMembersByRole(
            @PathVariable Long workspaceId,
            @PathVariable MemberRole role) {
        return ResponseEntity.ok(workspaceMemberService.getMembersByRole(workspaceId, role));
    }
}
