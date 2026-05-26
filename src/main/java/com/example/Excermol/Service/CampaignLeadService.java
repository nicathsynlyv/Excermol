package com.example.Excermol.Service;

import com.example.Excermol.entity.CampaignLead;
import com.example.Excermol.entity.dtos.CampaignLeadRequestDTO;
import com.example.Excermol.entity.dtos.CampaignLeadResponseDTO;

import java.util.List;

public interface CampaignLeadService  {
    CampaignLeadResponseDTO createLead(CampaignLeadRequestDTO requestDTO);

    List<CampaignLeadResponseDTO> getLeadsByCampaignId(Long campaignId);

    CampaignLeadResponseDTO updateLead(Long id, CampaignLeadRequestDTO requestDTO);

    void deleteLead(Long id);
}
