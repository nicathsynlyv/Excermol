package com.example.Excermol.controller;

import com.example.Excermol.Service.NotificationSettingService;
import com.example.Excermol.entity.dtos.NotificationSettingCreateRequestDTO;
import com.example.Excermol.entity.dtos.NotificationSettingResponseDTO;
import com.example.Excermol.entity.dtos.NotificationSettingUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification-settings")
@Tag(name = "NotificationSetting API", description = "Settings -> Notifications tab əməliyyatları")
public class NotificationSettingController {

    private final NotificationSettingService notificationSettingService;

    public NotificationSettingController(NotificationSettingService notificationSettingService) {
        this.notificationSettingService = notificationSettingService;
    }
//1
    @Operation(summary = "Yeni notification setting yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setting uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "Workspace və ya User tapılmadı"),
            @ApiResponse(responseCode = "400", description = "Bu user üçün artıq setting mövcuddur")
    })
    @PostMapping
    public ResponseEntity<NotificationSettingResponseDTO> createNotificationSetting(
           @Valid @RequestBody NotificationSettingCreateRequestDTO dto) {
        return ResponseEntity.ok(notificationSettingService.createNotificationSetting(dto));
    }
//2
    @Operation(summary = "Workspace-ə görə bütün settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<List<NotificationSettingResponseDTO>> getSettingsByWorkspaceId(
            @PathVariable Long workspaceId) {
        return ResponseEntity.ok(notificationSettingService.getSettingsByWorkspaceId(workspaceId));
    }
//3
    @Operation(summary = "User-ə görə bütün settings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationSettingResponseDTO>> getSettingsByUserId(
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationSettingService.getSettingsByUserId(userId));
    }
//4
    @Operation(summary = "Workspace və User-ə görə setting tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setting tapıldı"),
            @ApiResponse(responseCode = "404", description = "Setting tapılmadı")
    })
    @GetMapping("/workspace/{workspaceId}/user/{userId}")
    public ResponseEntity<NotificationSettingResponseDTO> getSettingByWorkspaceAndUser(
            @PathVariable Long workspaceId,
            @PathVariable Long userId) {
        return ResponseEntity.ok(notificationSettingService.getSettingByWorkspaceAndUser(workspaceId, userId));
    }
//5
    @Operation(summary = "ID ilə setting tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setting tapıldı"),
            @ApiResponse(responseCode = "404", description = "Setting tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificationSettingResponseDTO> getSettingById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationSettingService.getSettingById(id));
    }
//6
    @Operation(summary = "Checkboxları update et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Setting uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Setting tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<NotificationSettingResponseDTO> updateSetting(
            @PathVariable Long id,
           @Valid @RequestBody NotificationSettingUpdateRequestDTO dto) {
        return ResponseEntity.ok(notificationSettingService.updateSetting(id, dto));
    }
//7
    @Operation(summary = "Setting sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Setting uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Setting tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSetting(@PathVariable Long id) {
        notificationSettingService.deleteSetting(id);
        return ResponseEntity.noContent().build();
    }
}
