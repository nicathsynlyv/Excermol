package com.example.Excermol.controller;

import com.example.Excermol.Service.CampaignLeadService;
import com.example.Excermol.entity.CampaignLead;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaign-leads")
@RequiredArgsConstructor
public class CampaignLeadController {
    private final CampaignLeadService campaignLeadService;

    @PostMapping("/{campaignId}")
    public CampaignLead addLead(@PathVariable Long campaignId, @RequestBody CampaignLead lead) {
        return campaignLeadService.addLead(campaignId, lead);
    }

    @GetMapping("/{campaignId}")
    public List<CampaignLead> getLeads(@PathVariable Long campaignId) {
        return campaignLeadService.getLeadsByCampaign(campaignId);
    }

    @PutMapping("/{leadId}")
    public CampaignLead updateLead(@PathVariable Long leadId, @RequestBody CampaignLead lead) {
        return campaignLeadService.updateLeadStatus(leadId, lead);
    }

    @DeleteMapping("/{leadId}")
    public void deleteLead(@PathVariable Long leadId) {
        campaignLeadService.deleteLead(leadId);
    }
}
