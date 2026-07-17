package com.example.Excermol.controller;

import com.example.Excermol.Service.CampaignService;
import com.example.Excermol.entity.dtos.CampaignRequestDTO;
import com.example.Excermol.entity.dtos.CampaignResponseDto;
import com.example.Excermol.enums.CampaignStatus;
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
@RequestMapping("/api/campaigns")
@Tag(name = "Campaign Controller", description = "Campaign CRUD əməliyyatları")
public class CampaignController {

    private final CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @Operation(summary = "Yeni kampaniya yaradır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kampaniya uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = CampaignResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping
    public ResponseEntity<CampaignResponseDto> createCampaign(
            @Valid @RequestBody CampaignRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(campaignService.createCampaign(requestDTO));
    }

    @Operation(summary = "Bütün kampaniyaları gətirir")
    @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı",
            content = @Content(schema = @Schema(implementation = CampaignResponseDto.class)))
    @GetMapping
    public ResponseEntity<List<CampaignResponseDto>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAllCampaigns());
    }

    @Operation(summary = "ID ilə kampaniya gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kampaniya tapıldı",
                    content = @Content(schema = @Schema(implementation = CampaignResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Kampaniya tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CampaignResponseDto> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    @Operation(summary = "Kampaniyanı yeniləyir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kampaniya uğurla update olundu",
                    content = @Content(schema = @Schema(implementation = CampaignResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Kampaniya tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CampaignResponseDto> updateCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CampaignRequestDTO requestDTO) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, requestDTO));
    }

    @Operation(summary = "Kampaniyanı silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kampaniya uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Kampaniya tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }


    //user new changes
    @Operation(summary = "User-ə məxsus bütün kampaniyalar")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı"),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı"),
            @ApiResponse(responseCode = "500", description = "Daxili server xətası")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CampaignResponseDto>> getCampaignsByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(campaignService.getCampaignsByUser(userId));
    }

    //user new changes
    @Operation(summary = "User-ə məxsus kampaniyalar + status filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı"),
            @ApiResponse(responseCode = "400", description = "Yanlış status dəyəri göndərilib"),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı"),
            @ApiResponse(responseCode = "500", description = "Daxili server xətası")
    })
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<CampaignResponseDto>> getCampaignsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable CampaignStatus status) {
        return ResponseEntity.ok(campaignService.getCampaignsByUserAndStatus(userId, status));
    }
}