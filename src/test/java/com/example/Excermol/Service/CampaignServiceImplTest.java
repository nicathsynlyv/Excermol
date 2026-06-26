package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.CampaignServiceImpl;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CampaignServiceImpl Unit Tests")
class CampaignServiceImplTest {

    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private CampaignMapper campaignMapper;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CampaignServiceImpl campaignService;

    private Campaign campaign;
    private CampaignRequestDTO requestDTO;
    private CampaignResponseDto responseDto;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(1L);
        campaign.setName("Test Campaign");
        campaign.setStatus(CampaignStatus.ACTIVE);

        requestDTO = new CampaignRequestDTO();
        requestDTO.setName("Test Campaign");
        requestDTO.setStatus(CampaignStatus.ACTIVE);

        responseDto = new CampaignResponseDto();
        responseDto.setId(1L);
        responseDto.setName("Test Campaign");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createCampaign_shouldSaveAndReturnCampaign_withoutUser() {
        when(campaignMapper.toEntity(requestDTO)).thenReturn(campaign);
        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        CampaignResponseDto result = campaignService.createCampaign(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Test Campaign");
        verify(campaignRepository).save(campaign);
    }

    @Test
    void createCampaign_shouldSetUser_whenUserIdProvided() {
        requestDTO.setUserId(5L);
        User user = new User();
        user.setId(5L);

        when(campaignMapper.toEntity(requestDTO)).thenReturn(campaign);
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        campaignService.createCampaign(requestDTO);

        assertThat(campaign.getUser()).isEqualTo(user);
        verify(userRepository).findById(5L);
    }

    @Test
    void createCampaign_shouldThrowException_whenUserNotFound() {
        requestDTO.setUserId(5L);

        when(campaignMapper.toEntity(requestDTO)).thenReturn(campaign);
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> campaignService.createCampaign(requestDTO));
        verify(campaignRepository, never()).save(any());
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAllCampaigns_shouldReturnAllCampaigns() {
        when(campaignRepository.findAll()).thenReturn(List.of(campaign));
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        List<CampaignResponseDto> result = campaignService.getAllCampaigns();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Test Campaign");
        verify(campaignRepository).findAll();
    }

    @Test
    void getAllCampaigns_shouldReturnEmptyList_whenNoCampaigns() {
        when(campaignRepository.findAll()).thenReturn(List.of());

        List<CampaignResponseDto> result = campaignService.getAllCampaigns();

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getCampaignById_shouldReturnCampaign_whenExists() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        CampaignResponseDto result = campaignService.getCampaignById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(campaignRepository).findById(1L);
    }

    @Test
    void getCampaignById_shouldThrowException_whenNotFound() {
        when(campaignRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CampaignNotFoundException.class, () -> campaignService.getCampaignById(99L));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateCampaign_shouldUpdateAndReturnCampaign() {
        requestDTO.setName("Updated Campaign");

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        CampaignResponseDto result = campaignService.updateCampaign(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(campaignMapper).updateEntity(campaign, requestDTO);
        verify(campaignRepository).save(campaign);
    }

    @Test
    void updateCampaign_shouldThrowException_whenCampaignNotFound() {
        when(campaignRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CampaignNotFoundException.class, () -> campaignService.updateCampaign(99L, requestDTO));
        verify(campaignRepository, never()).save(any());
    }

    @Test
    void updateCampaign_shouldUpdateUser_whenUserIdProvided() {
        requestDTO.setUserId(5L);
        User user = new User();
        user.setId(5L);

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(campaignRepository.save(campaign)).thenReturn(campaign);
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        campaignService.updateCampaign(1L, requestDTO);

        assertThat(campaign.getUser()).isEqualTo(user);
    }

    @Test
    void updateCampaign_shouldThrowException_whenUserNotFound() {
        requestDTO.setUserId(5L);

        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> campaignService.updateCampaign(1L, requestDTO));
        verify(campaignRepository, never()).save(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteCampaign_shouldDeleteCampaign_whenExists() {
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaign));

        campaignService.deleteCampaign(1L);

        verify(campaignRepository).delete(campaign);
    }

    @Test
    void deleteCampaign_shouldThrowException_whenNotFound() {
        when(campaignRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CampaignNotFoundException.class, () -> campaignService.deleteCampaign(99L));
        verify(campaignRepository, never()).delete(any());
    }

    // =========================
    // GET BY USER
    // =========================
    @Test
    void getCampaignsByUser_shouldReturnFilteredCampaigns() {
        when(campaignRepository.findByUserId(5L)).thenReturn(List.of(campaign));
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        List<CampaignResponseDto> result = campaignService.getCampaignsByUser(5L);

        assertThat(result).hasSize(1);
        verify(campaignRepository).findByUserId(5L);
    }

    @Test
    void getCampaignsByUser_shouldReturnEmptyList_whenNoCampaigns() {
        when(campaignRepository.findByUserId(5L)).thenReturn(List.of());

        List<CampaignResponseDto> result = campaignService.getCampaignsByUser(5L);

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY USER AND STATUS
    // =========================
    @Test
    void getCampaignsByUserAndStatus_shouldReturnFilteredCampaigns() {
        when(campaignRepository.findByUserIdAndStatus(5L, CampaignStatus.ACTIVE)).thenReturn(List.of(campaign));
        when(campaignMapper.toResponseDTO(campaign)).thenReturn(responseDto);

        List<CampaignResponseDto> result = campaignService.getCampaignsByUserAndStatus(5L, CampaignStatus.ACTIVE);

        assertThat(result).hasSize(1);
        verify(campaignRepository).findByUserIdAndStatus(5L, CampaignStatus.ACTIVE);
    }

    @Test
    void getCampaignsByUserAndStatus_shouldReturnEmptyList_whenNoMatches() {
        when(campaignRepository.findByUserIdAndStatus(5L, CampaignStatus.ACTIVE)).thenReturn(List.of());

        List<CampaignResponseDto> result = campaignService.getCampaignsByUserAndStatus(5L, CampaignStatus.ACTIVE);

        assertThat(result).isEmpty();
    }
}