package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CampaignService;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.exception.CampaignNotFoundException;
import com.example.Excermol.repository.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements CampaignService {

    private final CampaignRepository campaignRepository;

    // BaseService metodları
    @Override
    public List<Campaign> getAll() {
        return campaignRepository.findAll();
    }

    @Override
    public Optional<Campaign> getById(Long id) {
        return campaignRepository.findById(id);
    }

    @Override
    public Campaign save(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    @Override
    public void deleteById(Long id) {
        campaignRepository.deleteById(id);
    }

    // Campaign update metodu
    public Campaign updateCampaign(Long id, Campaign updatedCampaign) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campaign ID = " + id + " tapılmadı!"));



        campaign.setName(updatedCampaign.getName());
        campaign.setSequenceStarted(updatedCampaign.getSequenceStarted());
        campaign.setOpenRate(updatedCampaign.getOpenRate());
        campaign.setReplyRate(updatedCampaign.getReplyRate());
        campaign.setBounceRate(updatedCampaign.getBounceRate());
        campaign.setStatus(updatedCampaign.getStatus());

        // Leads və Emails update lazımdırsa burada əlavə edə bilərsən
        // campaign.getLeads().clear();
        // if (updatedCampaign.getLeads() != null) {
        //     campaign.getLeads().addAll(updatedCampaign.getLeads());
        // }
        //
        // campaign.getEmails().clear();
        // if (updatedCampaign.getEmails() != null) {
        //     campaign.getEmails().addAll(updatedCampaign.getEmails());
        // }

        return campaignRepository.save(campaign);
    }
}
