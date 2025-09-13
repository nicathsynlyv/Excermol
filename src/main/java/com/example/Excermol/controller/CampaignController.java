package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.CampaignServiceImpl;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.exception.CampaignNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignServiceImpl campaignService;

    @Operation(summary = "Bütün kampaniyaları gətirir", description = "Bazada mövcud olan bütün kampaniyaları qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı",
                    content = @Content(schema = @Schema(implementation = Campaign.class)))
    })
    @GetMapping
    public ResponseEntity<List<Campaign>> getAllCampaigns() {
        return ResponseEntity.ok(campaignService.getAll());
    }

    @Operation(summary = "ID-ə görə kampaniya gətirir", description = "Verilmiş ID-ə əsasən kampaniyanı qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kampaniya tapıldı",
                    content = @Content(schema = @Schema(implementation = Campaign.class))),
            @ApiResponse(responseCode = "404", description = "Kampaniya tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Campaign> getCampaignById(@PathVariable Long id) {
        return campaignService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni kampaniya yaradır", description = "Verilən məlumatlarla yeni kampaniya əlavə edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kampaniya uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = Campaign.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Campaign> createCampaign(@RequestBody Campaign campaign) {
        return ResponseEntity.status(201).body(campaignService.save(campaign));
    }

    @Operation(summary = "Kampaniyanı yeniləyir", description = "Verilmiş ID-ə əsasən kampaniyanı update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Kampaniya uğurla update olundu",
                    content = @Content(schema = @Schema(implementation = Campaign.class))),
            @ApiResponse(responseCode = "404", description = "Kampaniya tapılmadı", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Campaign> updateCampaign(@PathVariable Long id,
                                                   @RequestBody Campaign updatedCampaign) {
        Campaign updated = campaignService.updateCampaign(id, updatedCampaign);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Kampaniyanı silir", description = "Verilmiş ID-ə əsasən kampaniyanı silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Kampaniya uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Kampaniya tapılmadı", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        Campaign campaign = campaignService.getById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign ID = " + id + " tapılmadı!"));
        campaignService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
