package com.example.Excermol.controller;

import com.example.Excermol.Service.CampaignService;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.enums.CampaignStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    @PostMapping
    public Campaign create(@RequestBody Campaign campaign) {
        return campaignService.createCampaign(campaign);
    }

    @GetMapping
    public List<Campaign> getAll() {
        return campaignService.getAllCampaigns();
    }

    @GetMapping("/{id}")
    public Campaign getById(@PathVariable Long id) {
        return campaignService.getCampaignById(id);
    }

    @PutMapping("/{id}")
    public Campaign update(@PathVariable Long id, @RequestBody Campaign campaign) {
        return campaignService.updateCampaign(id, campaign);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
    }
}
