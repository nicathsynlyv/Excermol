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
        return campaignService.create(campaign);
    }

    @PutMapping("/{id}")
    public Campaign update(@PathVariable Long id, @RequestBody Campaign campaign) {
        return campaignService.update(id, campaign);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        campaignService.delete(id);
    }

    @GetMapping("/{id}")
    public Campaign getById(@PathVariable Long id) {
        return campaignService.getById(id);
    }

    @GetMapping
    public List<Campaign> getAll(@RequestParam(defaultValue = "name") String sortBy) {
        return campaignService.getAll(sortBy);
    }

    @GetMapping("/status/{status}")
    public List<Campaign> getByStatus(@PathVariable CampaignStatus status) {
        return campaignService.getByStatus(status);
    }

    @GetMapping("/search")
    public List<Campaign> search(@RequestParam String keyword) {
        return campaignService.searchByName(keyword);
    }
}
