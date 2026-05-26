package com.example.Excermol.mapper;

import com.example.Excermol.entity.Organization;
import com.example.Excermol.entity.dtos.OrganizationRequestDTO;
import com.example.Excermol.entity.dtos.OrganizationResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class OrganizationMapper {

    // RequestDTO -> Entity
    public Organization toEntity(OrganizationRequestDTO requestDTO) {

        Organization organization = new Organization();

        organization.setName(requestDTO.getName());
        organization.setDomain(requestDTO.getDomain());
        organization.setDescription(requestDTO.getDescription());
        organization.setListName(requestDTO.getListName());
        organization.setNumberOfDeals(requestDTO.getNumberOfDeals());
        organization.setEmployeesRange(requestDTO.getEmployeesRange());

        return organization;
    }

    // UPDATE METHOD
    public void updateEntity(Organization organization,
                             OrganizationRequestDTO requestDTO) {

        organization.setName(requestDTO.getName());
        organization.setDomain(requestDTO.getDomain());
        organization.setDescription(requestDTO.getDescription());
        organization.setListName(requestDTO.getListName());
        organization.setNumberOfDeals(requestDTO.getNumberOfDeals());
        organization.setEmployeesRange(requestDTO.getEmployeesRange());
    }

    // Entity -> ResponseDTO
    public OrganizationResponseDTO toResponseDTO(Organization organization) {

        OrganizationResponseDTO responseDTO =
                new OrganizationResponseDTO();

        responseDTO.setId(organization.getId());
        responseDTO.setName(organization.getName());
        responseDTO.setDomain(organization.getDomain());
        responseDTO.setDescription(organization.getDescription());
        responseDTO.setListName(organization.getListName());
        responseDTO.setNumberOfDeals(organization.getNumberOfDeals());
        responseDTO.setEmployeesRange(organization.getEmployeesRange());
        responseDTO.setCreatedAt(organization.getCreatedAt());
        responseDTO.setLastInteractedAt(
                organization.getLastInteractedAt());

        if (organization.getManager() != null) {

            responseDTO.setManagerId(
                    organization.getManager().getId());

            responseDTO.setManagerName(
                    organization.getManager().getFullName());
        }

        return responseDTO;
    }
}
