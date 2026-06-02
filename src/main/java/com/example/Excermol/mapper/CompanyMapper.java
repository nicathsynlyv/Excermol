package com.example.Excermol.mapper;

import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.dtos.CompanyRequestDTO;
import com.example.Excermol.entity.dtos.CompanyResponseDTO;

import org.springframework.stereotype.Component;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CompanyMapper {

    // RequestDTO -> Entity
    public Company toEntity(CompanyRequestDTO requestDTO) {
        Company company = new Company();
        company.setCompanyName(requestDTO.getCompanyName());
        company.setDomain(requestDTO.getDomain());
        company.setEmailAddress(requestDTO.getEmailAddress());
        company.setLeadSource(requestDTO.getLeadSource());
        company.setCity(requestDTO.getCity());
        company.setConnection(requestDTO.getConnection());
        company.setLeadValue(requestDTO.getLeadValue());
        if (requestDTO.getStatus() != null) {
            company.setStatus(requestDTO.getStatus());
        }
        // ownerId, userId → service-də set edirik
        return company;
    }

    // Entity -> ResponseDTO
    public CompanyResponseDTO toResponseDTO(Company company) {
        CompanyResponseDTO responseDTO = new CompanyResponseDTO();
        responseDTO.setId(company.getId());
        responseDTO.setCompanyName(company.getCompanyName());
        responseDTO.setDomain(company.getDomain());
        responseDTO.setEmailAddress(company.getEmailAddress());
        responseDTO.setLeadSource(company.getLeadSource());
        responseDTO.setCity(company.getCity());
        responseDTO.setConnection(company.getConnection());
        responseDTO.setLeadValue(company.getLeadValue());
        responseDTO.setStatus(company.getStatus());

        // ✅ Owner null yoxlaması
        if (company.getOwner() != null) {
            String ownerName = company.getOwner().getFullName();
            if (company.getOwner().getLastName() != null) {
                ownerName += " " + company.getOwner().getLastName();
            }
            responseDTO.setOwnerId(company.getOwner().getId());
            responseDTO.setOwnerName(ownerName);
        }

        // User ← Ynew changes
        if (company.getUser() != null) {
            responseDTO.setUserId(company.getUser().getId());
        }

        return responseDTO;
    }

    // Update metodu
    public void updateEntity(Company company, CompanyRequestDTO requestDTO) {
        company.setCompanyName(requestDTO.getCompanyName());
        company.setDomain(requestDTO.getDomain());
        company.setEmailAddress(requestDTO.getEmailAddress());
        company.setLeadSource(requestDTO.getLeadSource());
        company.setCity(requestDTO.getCity());
        company.setConnection(requestDTO.getConnection());
        company.setLeadValue(requestDTO.getLeadValue());
        if (requestDTO.getStatus() != null) {
            company.setStatus(requestDTO.getStatus());
        }
    }

    // ✅ List üçün köməkçi metod
    public List<CompanyResponseDTO> toResponseList(List<Company> companies) {
        if (companies == null) {
            return Collections.emptyList();
        }
        return companies.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}