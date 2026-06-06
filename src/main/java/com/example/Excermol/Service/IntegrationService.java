package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.IntegrationCreateRequestDTO;
import com.example.Excermol.entity.dtos.IntegrationResponseDTO;
import com.example.Excermol.entity.dtos.IntegrationUpdateRequestDTO;
import com.example.Excermol.enums.IntegrationType;

import java.util.List;

public interface IntegrationService {
    // integration yarat
    IntegrationResponseDTO createIntegration(IntegrationCreateRequestDTO dto);

    // workspace-ə görə bütün integration-lar
    List<IntegrationResponseDTO> getIntegrationsByWorkspaceId(Long workspaceId);

    // id-yə görə integration
    IntegrationResponseDTO getIntegrationById(Long id);

    // workspace və tipə görə integration tap
    IntegrationResponseDTO getIntegrationByType(Long workspaceId, IntegrationType type);

    // aktiv integration-lar
    List<IntegrationResponseDTO> getActiveIntegrations(Long workspaceId);

    // integration update et - description, toggle
    IntegrationResponseDTO updateIntegration(Long id, IntegrationUpdateRequestDTO dto);

    // toggle - aktiv/deaktiv et
    IntegrationResponseDTO toggleIntegration(Long id);

    // integration sil
    void deleteIntegration(Long id);
}
