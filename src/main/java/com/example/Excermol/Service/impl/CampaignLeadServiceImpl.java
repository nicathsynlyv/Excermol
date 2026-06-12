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

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
@Slf4j
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
        log.info("Creating campaign lead for campaign id: {}", requestDTO.getCampaignId());
        // Campaign mövcuddurmu yoxla
        Campaign campaign = campaignRepository.findById(requestDTO.getCampaignId())
                .orElseThrow(() -> {
                    log.warn("Campaign not found with id: {}", requestDTO.getCampaignId());
                    return new CampaignNotFoundException("Campaign tapılmadı! ID: " + requestDTO.getCampaignId());
                });

        CampaignLead lead = campaignMapper.toLeadEntity(requestDTO);
        lead.setCampaign(campaign);
        lead.setLastActive(LocalDate.now()); // ← create zamanı da set et


        // Company mövcuddurmu yoxla
        if (requestDTO.getCompanyId() != null) {
            log.info("Setting company with id: {}", requestDTO.getCompanyId());
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> {
                        log.warn("Company not found with id: {}", requestDTO.getCompanyId());
                        return new CompanyNotFoundException("Company tapılmadı! ID: " + requestDTO.getCompanyId());
                    });
            lead.setCompany(company);
        }

        CampaignLeadResponseDTO response = campaignMapper.toLeadResponseDTO(campaignLeadRepository.save(lead));
        log.info("Campaign lead created successfully with id: {}", response.getId());
        return response;    }

    @Override
    public List<CampaignLeadResponseDTO> getLeadsByCampaignId(Long campaignId) {
        log.info("Fetching leads for campaign id: {}", campaignId);
        if (!campaignRepository.existsById(campaignId)) {
            log.warn("Campaign not found with id: {}", campaignId);
            throw new CampaignNotFoundException("Campaign tapılmadı! ID: " + campaignId);
        }

        List<CampaignLeadResponseDTO> leads = campaignLeadRepository.findByCampaignId(campaignId)
                .stream()
                .map(campaignMapper::toLeadResponseDTO)
                .toList();

        log.info("Retrieved {} leads for campaign id: {}", leads.size(), campaignId);
        return leads;
    }

    @Override
    public CampaignLeadResponseDTO updateLead(Long id, CampaignLeadRequestDTO requestDTO) {
        log.info("Updating campaign lead with id: {}",id);
        CampaignLead lead = campaignLeadRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Lead not found for update. Id: {}", id);
                    return new LeadNotFoundException("Lead tapılmadı! ID: " + id);
                });

        campaignMapper.updateLeadEntity(lead, requestDTO);
        lead.setLastActive(LocalDate.now());

        if (requestDTO.getCompanyId() != null) {
            log.info("Updating company with id: {}", requestDTO.getCompanyId());
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> {
                        log.warn("Company not found with id: {}", requestDTO.getCompanyId());
                        return new CompanyNotFoundException("Company tapılmadı! ID: " + requestDTO.getCompanyId());
                    });
            lead.setCompany(company);
        }

        CampaignLeadResponseDTO response = campaignMapper.toLeadResponseDTO(campaignLeadRepository.save(lead));
        log.info("Campaign lead updated successfully. Id: {}", id);
        return response;    }

    @Override
    public void deleteLead(Long id) {
        log.info("Deleting campaign lead with id: {}", id);
        CampaignLead lead = campaignLeadRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Lead not found for deletion. Id: {}", id);
                    return new LeadNotFoundException("Lead tapılmadı! ID: " + id);
                });

        campaignLeadRepository.delete(lead);
        log.info("Campaign lead deleted successfully. Id: {}", id);
    }
}
