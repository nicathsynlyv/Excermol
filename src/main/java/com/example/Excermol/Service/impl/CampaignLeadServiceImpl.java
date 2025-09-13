package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CampaignLeadService;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.CampaignLead;
import com.example.Excermol.exception.CampaignNotFoundException;
import com.example.Excermol.exception.LeadNotFoundException;
import com.example.Excermol.repository.CampaignLeadRepository;
import com.example.Excermol.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignLeadServiceImpl implements CampaignLeadService {

    private final CampaignLeadRepository campaignLeadRepository;
    private final CampaignRepository campaignRepository;

    // BaseService metodları
    @Override
    public List<CampaignLead> getAll() {
        return campaignLeadRepository.findAll();
    }

    @Override
    public Optional<CampaignLead> getById(Long id) {
        return campaignLeadRepository.findById(id);
    }

    @Override
    public CampaignLead save(CampaignLead lead) {
        return campaignLeadRepository.save(lead);
    }

    @Override
    public void deleteById(Long id) {
        campaignLeadRepository.deleteById(id);
    }

    // CampaignLead spesifik metod
    @Override
    public List<CampaignLead> findByCampaignId(Long campaignId) {
        return campaignLeadRepository.findByCampaignId(campaignId);
    }

    // Lead əlavə etmək
    public CampaignLead addLead(Long campaignId, CampaignLead lead) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign ID = " + campaignId + " tapılmadı!"));
        lead.setCampaign(campaign);
        return campaignLeadRepository.save(lead);
    }

    // Lead update etmək
    public CampaignLead updateLeadStatus(Long leadId, CampaignLead updatedLead) {
        CampaignLead lead = campaignLeadRepository.findById(leadId)
                .orElseThrow(() -> new LeadNotFoundException("Lead ID = " + leadId + " tapılmadı!"));
        lead.setStatus(updatedLead.getStatus());
        lead.setProgress(updatedLead.getProgress());
        lead.setLastActive(updatedLead.getLastActive());
        return campaignLeadRepository.save(lead);
    }
}
