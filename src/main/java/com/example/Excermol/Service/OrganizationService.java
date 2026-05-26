package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.OrganizationRequestDTO;
import com.example.Excermol.entity.dtos.OrganizationResponseDTO;

import java.util.List;

public interface OrganizationService {
    OrganizationResponseDTO createOrganization(OrganizationRequestDTO requestDTO);

    List<OrganizationResponseDTO> getAllOrganizations();

    OrganizationResponseDTO getOrganizationById(Long id);

    OrganizationResponseDTO updateOrganization(Long id, OrganizationRequestDTO requestDTO);

    void deleteOrganization(Long id);
}
