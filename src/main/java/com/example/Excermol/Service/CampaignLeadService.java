package com.example.Excermol.Service;

import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.CampaignLead;
import com.example.Excermol.repository.CampaignLeadRepository;
import com.example.Excermol.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignLeadService {

    private final CampaignLeadRepository campaignLeadRepository;
    private final CampaignRepository campaignRepository;

    public CampaignLead addLead(Long campaignId, CampaignLead lead) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
        lead.setCampaign(campaign);
        return campaignLeadRepository.save(lead);
    }

    public List<CampaignLead> getLeadsByCampaign(Long campaignId) {
        return campaignLeadRepository.findByCampaignId(campaignId);
    }

    public CampaignLead updateLeadStatus(Long leadId, CampaignLead updatedLead) {
        CampaignLead lead = campaignLeadRepository.findById(leadId)
                .orElseThrow(() -> new RuntimeException("Lead not found"));
        lead.setStatus(updatedLead.getStatus());
        lead.setProgress(updatedLead.getProgress());
        lead.setLastActive(updatedLead.getLastActive());
        return campaignLeadRepository.save(lead);
    }

    public void deleteLead(Long leadId) {
        campaignLeadRepository.deleteById(leadId);
    }
}
