package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CampaignLeadService;
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
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional

public class CampaignLeadServiceImpl implements CampaignLeadService {

    private final CampaignLeadRepository campaignLeadRepository;
    private final CampaignRepository campaignRepository;
    private final CompanyRepository companyRepository;
    private final CampaignMapper campaignMapper;

    public CampaignLeadServiceImpl(CampaignLeadRepository campaignLeadRepository,
                                   CampaignRepository campaignRepository,
                                   CompanyRepository companyRepository,
                                   CampaignMapper campaignMapper) {
        this.campaignLeadRepository = campaignLeadRepository;
        this.campaignRepository = campaignRepository;
        this.companyRepository = companyRepository;
        this.campaignMapper = campaignMapper;
    }

    @Override
    public CampaignLeadResponseDTO createLead(CampaignLeadRequestDTO requestDTO) {
        // Campaign mövcuddurmu yoxla
        Campaign campaign = campaignRepository.findById(requestDTO.getCampaignId())
                .orElseThrow(() -> new CampaignNotFoundException(
                        "Campaign tapılmadı! ID: " + requestDTO.getCampaignId()));

        CampaignLead lead = campaignMapper.toLeadEntity(requestDTO);
        lead.setCampaign(campaign);
        lead.setLastActive(LocalDate.now()); // ← create zamanı da set et


        // Company mövcuddurmu yoxla
        if (requestDTO.getCompanyId() != null) {
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(
                            "Company tapılmadı! ID: " + requestDTO.getCompanyId()));
            lead.setCompany(company);
        }

        return campaignMapper.toLeadResponseDTO(campaignLeadRepository.save(lead));
    }

    @Override
    public List<CampaignLeadResponseDTO> getLeadsByCampaignId(Long campaignId) {
        if (!campaignRepository.existsById(campaignId)) {
            throw new CampaignNotFoundException("Campaign tapılmadı! ID: " + campaignId);
        }

        return campaignLeadRepository.findByCampaignId(campaignId)
                .stream()
                .map(campaignMapper::toLeadResponseDTO)
                .toList();
    }

    @Override
    public CampaignLeadResponseDTO updateLead(Long id, CampaignLeadRequestDTO requestDTO) {
        CampaignLead lead = campaignLeadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead tapılmadı! ID: " + id));

        campaignMapper.updateLeadEntity(lead, requestDTO);
        lead.setLastActive(LocalDate.now());

        if (requestDTO.getCompanyId() != null) {
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(
                            "Company tapılmadı! ID: " + requestDTO.getCompanyId()));
            lead.setCompany(company);
        }

        return campaignMapper.toLeadResponseDTO(campaignLeadRepository.save(lead));
    }

    @Override
    public void deleteLead(Long id) {
        CampaignLead lead = campaignLeadRepository.findById(id)
                .orElseThrow(() -> new LeadNotFoundException("Lead tapılmadı! ID: " + id));

        campaignLeadRepository.delete(lead);
    }
}
