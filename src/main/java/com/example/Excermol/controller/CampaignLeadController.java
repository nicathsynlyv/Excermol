package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.CampaignLeadServiceImpl;
import com.example.Excermol.entity.CampaignLead;
import com.example.Excermol.exception.LeadNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leads")
public class CampaignLeadController {

    private final CampaignLeadServiceImpl campaignleadServiceImpl;

    public CampaignLeadController(CampaignLeadServiceImpl campaignleadServiceImpl) {
        this.campaignleadServiceImpl = campaignleadServiceImpl;
    }

    @Operation(summary = "Bütün leads-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi",
                    content = @Content(schema = @Schema(implementation = CampaignLead.class)))
    })
    @GetMapping
    public ResponseEntity<List<CampaignLead>> getAllLeads() {
        return ResponseEntity.ok(campaignleadServiceImpl.getAll());
    }

    @Operation(summary = "ID-ə görə lead gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lead tapıldı",
                    content = @Content(schema = @Schema(implementation = CampaignLead.class))),
            @ApiResponse(responseCode = "404", description = "Lead tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<CampaignLead> getLeadById(@PathVariable Long id) {
        return campaignleadServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni lead əlavə edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lead uğurla əlavə edildi",
                    content = @Content(schema = @Schema(implementation = CampaignLead.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PostMapping("/campaign/{campaignId}")
    public ResponseEntity<CampaignLead> addLead(@PathVariable Long campaignId, @RequestBody CampaignLead lead) {
        return ResponseEntity.status(201).body(campaignleadServiceImpl.addLead(campaignId, lead));
    }

    @Operation(summary = "Lead update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lead uğurla update olundu",
                    content = @Content(schema = @Schema(implementation = CampaignLead.class))),
            @ApiResponse(responseCode = "404", description = "Lead tapılmadı", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<CampaignLead> updateLead(@PathVariable Long id, @RequestBody CampaignLead updatedLead) {
        CampaignLead updated = campaignleadServiceImpl.updateLeadStatus(id, updatedLead);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Lead silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lead uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lead tapılmadı", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLead(@PathVariable Long id) {
        CampaignLead lead = campaignleadServiceImpl.getById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead ID = " + id + " tapılmadı!"));
        campaignleadServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Kampaniyaya görə leads-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi",
                    content = @Content(schema = @Schema(implementation = CampaignLead.class)))
    })
    @GetMapping("/campaign/{campaignId}")
    public ResponseEntity<List<CampaignLead>> getLeadsByCampaign(@PathVariable Long campaignId) {
        return ResponseEntity.ok(campaignleadServiceImpl.findByCampaignId(campaignId));
    }
}
