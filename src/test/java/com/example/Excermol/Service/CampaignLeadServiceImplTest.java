package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.CampaignLeadServiceImpl;
import com.example.Excermol.entity.Campaign;
import com.example.Excermol.entity.CampaignLead;
import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.dtos.CampaignLeadRequestDTO;
import com.example.Excermol.entity.dtos.CampaignLeadResponseDTO;
import com.example.Excermol.exception.CampaignNotFoundException;
import com.example.Excermol.exception.CompanyNotFoundException;
import com.example.Excermol.exception.LeadNotFoundException;
import com.example.Excermol.mapper.CampaignMapper;
import com.example.Excermol.repository.CampaignLeadRepository;
import com.example.Excermol.repository.CampaignRepository;
import com.example.Excermol.repository.CompanyRepository;
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
@DisplayName("CampaignLeadServiceImpl Unit Tests")
public class CampaignLeadServiceImplTest {
    @Mock
    private CampaignLeadRepository campaignLeadRepository;
    @Mock
    private CampaignRepository campaignRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CampaignMapper campaignMapper;

    @InjectMocks
    private CampaignLeadServiceImpl campaignLeadService;

    private CampaignLead lead;
    private CampaignLeadRequestDTO requestDTO;
    private CampaignLeadResponseDTO responseDTO;
    private Campaign campaign;

    @BeforeEach
    void setUp() {
        campaign = new Campaign();
        campaign.setId(10L);
        campaign.setName("Test Campaign");

        lead = new CampaignLead();
        lead.setId(1L);
        lead.setLeadName("John Doe");
        lead.setLeadEmail("john@example.com");

        requestDTO = new CampaignLeadRequestDTO();
        requestDTO.setCampaignId(10L);
        requestDTO.setLeadName("John Doe");
        requestDTO.setLeadEmail("john@example.com");

        responseDTO = new CampaignLeadResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setLeadName("John Doe");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createLead_shouldSaveAndReturnLead_withoutCompany() {
        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));
        when(campaignMapper.toLeadEntity(requestDTO)).thenReturn(lead);
        when(campaignLeadRepository.save(lead)).thenReturn(lead);
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        CampaignLeadResponseDTO result = campaignLeadService.createLead(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getLeadName()).isEqualTo("John Doe");
        assertThat(lead.getCampaign()).isEqualTo(campaign);
        assertThat(lead.getLastActive()).isNotNull();
        verify(campaignLeadRepository).save(lead);
    }

    @Test
    void createLead_shouldThrowException_whenCampaignNotFound() {
        when(campaignRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CampaignNotFoundException.class, () -> campaignLeadService.createLead(requestDTO));
        verify(campaignLeadRepository, never()).save(any());
    }

    @Test
    void createLead_shouldSetCompany_whenCompanyIdProvided() {
        requestDTO.setCompanyId(20L);
        Company company = new Company();
        company.setId(20L);

        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));
        when(campaignMapper.toLeadEntity(requestDTO)).thenReturn(lead);
        when(companyRepository.findById(20L)).thenReturn(Optional.of(company));
        when(campaignLeadRepository.save(lead)).thenReturn(lead);
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        campaignLeadService.createLead(requestDTO);

        assertThat(lead.getCompany()).isEqualTo(company);
        verify(companyRepository).findById(20L);
    }

    @Test
    void createLead_shouldThrowException_whenCompanyNotFound() {
        requestDTO.setCompanyId(20L);

        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));
        when(campaignMapper.toLeadEntity(requestDTO)).thenReturn(lead);
        when(companyRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> campaignLeadService.createLead(requestDTO));
        verify(campaignLeadRepository, never()).save(any());
    }

    @Test
    void createLead_shouldNotCheckCompany_whenCompanyIdIsNull() {
        when(campaignRepository.findById(10L)).thenReturn(Optional.of(campaign));
        when(campaignMapper.toLeadEntity(requestDTO)).thenReturn(lead);
        when(campaignLeadRepository.save(lead)).thenReturn(lead);
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        campaignLeadService.createLead(requestDTO);

        verify(companyRepository, never()).findById(any());
        assertThat(lead.getCompany()).isNull();
    }

    // =========================
    // GET BY CAMPAIGN ID
    // =========================
    @Test
    void getLeadsByCampaignId_shouldReturnLeads_whenCampaignExists() {
        when(campaignRepository.existsById(10L)).thenReturn(true);
        when(campaignLeadRepository.findByCampaignId(10L)).thenReturn(List.of(lead));
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        List<CampaignLeadResponseDTO> result = campaignLeadService.getLeadsByCampaignId(10L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getLeadName()).isEqualTo("John Doe");
        verify(campaignLeadRepository).findByCampaignId(10L);
    }

    @Test
    void getLeadsByCampaignId_shouldReturnEmptyList_whenNoLeads() {
        when(campaignRepository.existsById(10L)).thenReturn(true);
        when(campaignLeadRepository.findByCampaignId(10L)).thenReturn(List.of());

        List<CampaignLeadResponseDTO> result = campaignLeadService.getLeadsByCampaignId(10L);

        assertThat(result).isEmpty();
    }

    @Test
    void getLeadsByCampaignId_shouldThrowException_whenCampaignNotFound() {
        when(campaignRepository.existsById(10L)).thenReturn(false);

        assertThrows(CampaignNotFoundException.class, () -> campaignLeadService.getLeadsByCampaignId(10L));
        verify(campaignLeadRepository, never()).findByCampaignId(any());
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateLead_shouldUpdateAndReturnLead_withoutCompany() {
        when(campaignLeadRepository.findById(1L)).thenReturn(Optional.of(lead));
        when(campaignLeadRepository.save(lead)).thenReturn(lead);
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        CampaignLeadResponseDTO result = campaignLeadService.updateLead(1L, requestDTO);

        assertThat(result).isNotNull();
        assertThat(lead.getLastActive()).isNotNull();
        verify(campaignMapper).updateLeadEntity(lead, requestDTO);
        verify(campaignLeadRepository).save(lead);
    }

    @Test
    void updateLead_shouldThrowException_whenLeadNotFound() {
        when(campaignLeadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LeadNotFoundException.class, () -> campaignLeadService.updateLead(99L, requestDTO));
        verify(campaignLeadRepository, never()).save(any());
    }

    @Test
    void updateLead_shouldUpdateCompany_whenCompanyIdProvided() {
        requestDTO.setCompanyId(20L);
        Company company = new Company();
        company.setId(20L);

        when(campaignLeadRepository.findById(1L)).thenReturn(Optional.of(lead));
        when(companyRepository.findById(20L)).thenReturn(Optional.of(company));
        when(campaignLeadRepository.save(lead)).thenReturn(lead);
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        campaignLeadService.updateLead(1L, requestDTO);

        assertThat(lead.getCompany()).isEqualTo(company);
    }

    @Test
    void updateLead_shouldThrowException_whenCompanyNotFound() {
        requestDTO.setCompanyId(20L);

        when(campaignLeadRepository.findById(1L)).thenReturn(Optional.of(lead));
        when(companyRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> campaignLeadService.updateLead(1L, requestDTO));
        verify(campaignLeadRepository, never()).save(any());
    }

    @Test
    void updateLead_shouldNotCheckCompany_whenCompanyIdIsNull() {
        when(campaignLeadRepository.findById(1L)).thenReturn(Optional.of(lead));
        when(campaignLeadRepository.save(lead)).thenReturn(lead);
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        campaignLeadService.updateLead(1L, requestDTO);

        verify(companyRepository, never()).findById(any());
    }

    @Test
    void updateLead_shouldSetLastActive_onUpdate() {
        when(campaignLeadRepository.findById(1L)).thenReturn(Optional.of(lead));
        when(campaignLeadRepository.save(lead)).thenReturn(lead);
        when(campaignMapper.toLeadResponseDTO(lead)).thenReturn(responseDTO);

        campaignLeadService.updateLead(1L, requestDTO);

        assertThat(lead.getLastActive()).isNotNull();
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteLead_shouldDeleteLead_whenExists() {
        when(campaignLeadRepository.findById(1L)).thenReturn(Optional.of(lead));

        campaignLeadService.deleteLead(1L);

        verify(campaignLeadRepository).delete(lead);
    }

    @Test
    void deleteLead_shouldThrowException_whenNotFound() {
        when(campaignLeadRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LeadNotFoundException.class, () -> campaignLeadService.deleteLead(99L));
        verify(campaignLeadRepository, never()).delete(any());
    }
}
