package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.CampaignRequestDTO;
import com.example.Excermol.entity.dtos.CampaignResponseDto;
import com.example.Excermol.enums.CampaignStatus;

import java.util.List;

public interface CampaignService  {
    CampaignResponseDto createCampaign(CampaignRequestDTO requestDTO);

    List<CampaignResponseDto> getAllCampaigns();

    CampaignResponseDto getCampaignById(Long id);

    CampaignResponseDto updateCampaign(Long id, CampaignRequestDTO requestDTO);

    void deleteCampaign(Long id);



    // User ← new changes
    List<CampaignResponseDto> getCampaignsByUser(Long userId);
    List<CampaignResponseDto> getCampaignsByUserAndStatus(Long userId, CampaignStatus status);
}
