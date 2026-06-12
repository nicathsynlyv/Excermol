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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@Slf4j
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
        log.info("Creating campaign with name: {}", requestDTO.getName());
        Campaign campaign = campaignMapper.toEntity(requestDTO);

        // User ← new changes
        if (requestDTO.getUserId() != null) {
            log.info("Setting user with id: {}", requestDTO.getUserId());
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        log.warn("User not found with id: {}", requestDTO.getUserId());
                        return new RuntimeException("User tapılmadı! ID: " + requestDTO.getUserId());
                    });
            campaign.setUser(user);
        }
        CampaignResponseDto response = campaignMapper.toResponseDTO(campaignRepository.save(campaign));
        log.info("Campaign created successfully with id: {}", response.getId());
        return response;
    }

    @Override
    public List<CampaignResponseDto> getAllCampaigns() {
        log.info("Fetching all campaigns");
        List<CampaignResponseDto> campaigns = campaignRepository.findAll()
                .stream()
                .map(campaignMapper::toResponseDTO)
                .toList();

        log.info("Retrieved {} campaigns", campaigns.size());
        return campaigns;
    }

    @Override
    public CampaignResponseDto getCampaignById(Long id) {
        log.info("Fetching campaign with id: {}", id);
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Campaign not found with id: {}", id);
                    return new CampaignNotFoundException("Campaign tapılmadı! ID: " + id);
                });
        log.info("Campaign found with id: {}", id);
        return campaignMapper.toResponseDTO(campaign);
    }

    @Override
    public CampaignResponseDto updateCampaign(Long id, CampaignRequestDTO requestDTO) {
        log.info("Updating campaign with id: {}", id);
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Campaign not found for update. Id: {}", id);
                    return new CampaignNotFoundException("Campaign tapılmadı! ID: " + id);
                });

        campaignMapper.updateEntity(campaign, requestDTO);

        // User ← new changes
        if (requestDTO.getUserId() != null) {
            log.info("Updating user with id: {}", requestDTO.getUserId());
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        log.warn("User not found with id: {}", requestDTO.getUserId());
                        return new UserNotFoundException("User tapılmadı! ID: " + requestDTO.getUserId());
                    });
            campaign.setUser(user);
        }

        CampaignResponseDto response = campaignMapper.toResponseDTO(campaignRepository.save(campaign));
        log.info("Campaign updated successfully. Id: {}", id);
        return response;
    }

    @Override
    public void deleteCampaign(Long id) {
        log.info("Deleting campaign with id: {}", id);
        Campaign campaign = campaignRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Campaign not found for deletion. Id: {}", id);
                    return new CampaignNotFoundException("Campaign tapılmadı! ID: " + id);
                });

        campaignRepository.delete(campaign);
        log.info("Campaign deleted successfully. Id: {}", id);
    }

//user new changes
    @Override
    public List<CampaignResponseDto> getCampaignsByUser(Long userId) {
        log.info("Fetching campaigns for user id: {}", userId);
        List<CampaignResponseDto> campaigns = campaignRepository.findByUserId(userId)
                .stream()
                .map(campaignMapper::toResponseDTO)
                .toList();

        log.info("Retrieved {} campaigns for user id: {}", campaigns.size(), userId);
        return campaigns;
    }
//user new changes
    @Override
    public List<CampaignResponseDto> getCampaignsByUserAndStatus(Long userId, CampaignStatus status) {
        log.info("Fetching campaigns for user id: {} with status: {}", userId, status);
        List<CampaignResponseDto> campaigns = campaignRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(campaignMapper::toResponseDTO)
                .toList();

        log.info("Retrieved {} campaigns for user id: {} with status: {}", campaigns.size(), userId, status);
        return campaigns;
    }
}
