package com.example.Excermol.mapper;

import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.CampaignLead;
import com.example.Excermol.entity.dtos.CampaignLeadRequestDTO;
import com.example.Excermol.entity.dtos.CampaignLeadResponseDTO;
import com.example.Excermol.entity.dtos.CampaignRequestDTO;
import com.example.Excermol.entity.dtos.CampaignResponseDto;
import com.example.Excermol.enums.LeadStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CampaignMapper {  // CampaignRequestDTO -> Entity
    public Campaign toEntity(CampaignRequestDTO requestDTO) {
        Campaign campaign = new Campaign();
        campaign.setName(requestDTO.getName());
        campaign.setStatus(requestDTO.getStatus());
        return campaign;
        // userId → service-də set edirik

    }

    // Campaign Entity -> ResponseDTO
    public CampaignResponseDto toResponseDTO(Campaign campaign) {
        CampaignResponseDto responseDTO = new CampaignResponseDto();
        responseDTO.setId(campaign.getId());
        responseDTO.setName(campaign.getName());
        responseDTO.setStatus(campaign.getStatus());
        responseDTO.setCreatedAt(campaign.getCreatedAt());

        // User ← new changes
        if (campaign.getUser() != null) {
            responseDTO.setUserId(campaign.getUser().getId());
        }

        // Statistikaları leads-dən hesabla
        List<CampaignLead> leads = campaign.getLeads();

        if (leads != null && !leads.isEmpty()) {
            int total = leads.size();

            responseDTO.setSequenceStarted(total);

            double openRate = leads.stream()
                    .filter(l -> l.getStatus() == LeadStatus.OPENED)
                    .count() * 100.0 / total;
            responseDTO.setOpenRate(Math.round(openRate * 10.0) / 10.0);

            double replyRate = leads.stream()
                    .filter(l -> l.getStatus() == LeadStatus.REPLIED)
                    .count() * 100.0 / total;
            responseDTO.setReplyRate(Math.round(replyRate * 10.0) / 10.0);

            double bounceRate = leads.stream()
                    .filter(l -> l.getStatus() == LeadStatus.BOUNCED)
                    .count() * 100.0 / total;
            responseDTO.setBounceRate(Math.round(bounceRate * 10.0) / 10.0);
        } else {
            responseDTO.setSequenceStarted(0);
            responseDTO.setOpenRate(0.0);
            responseDTO.setReplyRate(0.0);
            responseDTO.setBounceRate(0.0);
        }

        return responseDTO;
    }

    // CampaignLeadRequestDTO -> Entity
    public CampaignLead toLeadEntity(CampaignLeadRequestDTO requestDTO) {
        CampaignLead lead = new CampaignLead();
        lead.setLeadName(requestDTO.getLeadName());
        lead.setLeadEmail(requestDTO.getLeadEmail());
        lead.setDate(requestDTO.getDate());
        lead.setProgress(requestDTO.getProgress());
        lead.setStatus(requestDTO.getStatus());
        // campaignId və companyId → service-də set edirik
        return lead;
    }

    // CampaignLead Entity -> ResponseDTO
    public CampaignLeadResponseDTO toLeadResponseDTO(CampaignLead lead) {
        CampaignLeadResponseDTO responseDTO = new CampaignLeadResponseDTO();
        responseDTO.setId(lead.getId());
        responseDTO.setLeadName(lead.getLeadName());
        responseDTO.setLeadEmail(lead.getLeadEmail());
        responseDTO.setDate(lead.getDate());
        responseDTO.setLastActive(lead.getLastActive());
        responseDTO.setProgress(lead.getProgress());
        responseDTO.setStatus(lead.getStatus());

        // Company null ola bilər
        if (lead.getCompany() != null) {
            responseDTO.setCompanyName(lead.getCompany().getCompanyName());
        }

        return responseDTO;
    }

    // UPDATE metodları
    public void updateEntity(Campaign campaign, CampaignRequestDTO requestDTO) {
        campaign.setName(requestDTO.getName());
        campaign.setStatus(requestDTO.getStatus());
    }

    public void updateLeadEntity(CampaignLead lead, CampaignLeadRequestDTO requestDTO) {
        lead.setLeadName(requestDTO.getLeadName());
        lead.setLeadEmail(requestDTO.getLeadEmail());
        lead.setDate(requestDTO.getDate());
        lead.setProgress(requestDTO.getProgress());
        lead.setStatus(requestDTO.getStatus());
    }
}
