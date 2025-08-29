package com.example.Excermol.Service;

import com.example.Excermol.entity.Campaign;

import com.example.Excermol.repository.CampaignRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CampaignService {

    private final CampaignRepository campaignRepository;

    public Campaign createCampaign(Campaign campaign) {
        return campaignRepository.save(campaign);
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepository.findAll();
    }

    public Campaign getCampaignById(Long id) {
        return campaignRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    public Campaign updateCampaign(Long id, Campaign updatedCampaign) {
        Campaign campaign = getCampaignById(id);
        campaign.setSequenceStarted(updatedCampaign.getSequenceStarted()); // 🔹 əlavə et
        campaign.setName(updatedCampaign.getName());
        campaign.setOpenRate(updatedCampaign.getOpenRate());
        campaign.setReplyRate(updatedCampaign.getReplyRate());
        campaign.setBounceRate(updatedCampaign.getBounceRate());
        campaign.setStatus(updatedCampaign.getStatus());

        // Leads update
//        campaign.getLeads().clear();
//        if (updatedCampaign.getLeads() != null) {
//            campaign.getLeads().addAll(updatedCampaign.getLeads());
//        }
//
//        // Emails update
//        campaign.getEmails().clear();
//        if (updatedCampaign.getEmails() != null) {
//            campaign.getEmails().addAll(updatedCampaign.getEmails());
//        }


        return campaignRepository.save(campaign);
    }

    public void deleteCampaign(Long id) {
        campaignRepository.deleteById(id);
    }
}
