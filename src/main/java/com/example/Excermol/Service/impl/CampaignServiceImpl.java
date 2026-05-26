package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CampaignService;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.dtos.CampaignRequestDTO;
import com.example.Excermol.entity.dtos.CampaignResponseDto;
import com.example.Excermol.exception.CampaignNotFoundException;
import com.example.Excermol.mapper.CampaignMapper;
import com.example.Excermol.repository.CampaignRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {


    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;

    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               CampaignMapper campaignMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
    }

    @Override
    public CampaignResponseDto createCampaign(CampaignRequestDTO requestDTO) {
        Campaign campaign = campaignMapper.toEntity(requestDTO);
        return campaignMapper.toResponseDTO(campaignRepository.save(campaign));
    }

    @Override
    public List<CampaignResponseDto> getAllCampaigns() {
        return campaignRepository.findAll()
                .stream()
                .map(campaignMapper::toResponseDTO)
                .toList();
    }

    @Override
    public CampaignResponseDto getCampaignById(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(
                        "Campaign tapılmadı! ID: " + id));

        return campaignMapper.toResponseDTO(campaign);
    }

    @Override
    public CampaignResponseDto updateCampaign(Long id, CampaignRequestDTO requestDTO) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(
                        "Campaign tapılmadı! ID: " + id));

        campaignMapper.updateEntity(campaign, requestDTO);

        return campaignMapper.toResponseDTO(campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(
                        "Campaign tapılmadı! ID: " + id));

        campaignRepository.delete(campaign);
    }
}
