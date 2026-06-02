package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CampaignService;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.CampaignRequestDTO;
import com.example.Excermol.entity.dtos.CampaignResponseDto;
import com.example.Excermol.enums.CampaignStatus;
import com.example.Excermol.exception.CampaignNotFoundException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.CampaignMapper;
import com.example.Excermol.repository.CampaignRepository;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
public class CampaignServiceImpl implements CampaignService {


    private final CampaignRepository campaignRepository;
    private final CampaignMapper campaignMapper;
    private final UserRepository userRepository;

    public CampaignServiceImpl(CampaignRepository campaignRepository,
                               CampaignMapper campaignMapper,
                               UserRepository userRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignMapper = campaignMapper;
        this.userRepository = userRepository;
    }

    @Override
    public CampaignResponseDto createCampaign(CampaignRequestDTO requestDTO) {
        Campaign campaign = campaignMapper.toEntity(requestDTO);

        // User ← new changes
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException(
                            "User tapılmadı! ID: " + requestDTO.getUserId()));
            campaign.setUser(user);
        }
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

        // User ← new changes
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User tapılmadı! ID: " + requestDTO.getUserId()));
            campaign.setUser(user);
        }

        return campaignMapper.toResponseDTO(campaignRepository.save(campaign));
    }

    @Override
    public void deleteCampaign(Long id) {
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> new CampaignNotFoundException(
                        "Campaign tapılmadı! ID: " + id));

        campaignRepository.delete(campaign);
    }

//user new changes
    @Override
    public List<CampaignResponseDto> getCampaignsByUser(Long userId) {
        return campaignRepository.findByUserId(userId)
                .stream()
                .map(campaignMapper::toResponseDTO)
                .toList();
    }
//user new changes
    @Override
    public List<CampaignResponseDto> getCampaignsByUserAndStatus(Long userId, CampaignStatus status) {
        return campaignRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(campaignMapper::toResponseDTO)
                .toList();
    }
}
