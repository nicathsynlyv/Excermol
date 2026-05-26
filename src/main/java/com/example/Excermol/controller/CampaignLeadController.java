package com.example.Excermol.controller;

import com.example.Excermol.Service.CampaignLeadService;
import com.example.Excermol.entity.dtos.CampaignLeadRequestDTO;
import com.example.Excermol.entity.dtos.CampaignLeadResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/leads")
@Tag(name = "CampaignLead Controller", description = "Campaign Lead CRUD əməliyyatları")
public class CampaignLeadController {

    private final CampaignLeadService campaignLeadService;

    public CampaignLeadController(CampaignLeadService campaignLeadService) {
        this.campaignLeadService = campaignLeadService;
    }

    @Operation(summary = "Yeni lead əlavə edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lead uğurla əlavə edildi",
                    content = @Content(schema = @Schema(implementation = CampaignLeadResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PostMapping
    public ResponseEntity<CampaignLeadResponseDTO> createLead(
            @Valid @RequestBody CampaignLeadRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(campaignLeadService.createLead(requestDTO));
    }

    @Operation(summary = "Kampaniyaya görə leads-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi",
                    content = @Content(schema = @Schema(implementation = CampaignLeadResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Campaign tapılmadı", content = @Content)
    })
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<CampaignLeadResponseDTO>> getLeadsByCampaign(
            @PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignLeadService.getLeadsByCampaignId(campaignId));
    }

    @Operation(summary = "Lead update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lead uğurla update olundu",
                    content = @Content(schema = @Schema(implementation = CampaignLeadResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Lead tapılmadı", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CampaignLeadResponseDTO> updateLead(
            @PathVariable Long id,
            @Valid @RequestBody CampaignLeadRequestDTO requestDTO) {
        return ResponseEntity.ok(campaignLeadService.updateLead(id, requestDTO));
    }

    @Operation(summary = "Lead silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lead uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lead tapılmadı", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        campaignLeadService.deleteLead(id);
        return ResponseEntity.noContent().build();
    }
}
