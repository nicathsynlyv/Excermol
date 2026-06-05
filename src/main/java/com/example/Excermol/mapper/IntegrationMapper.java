package com.example.Excermol.mapper;

import com.example.Excermol.entity.Integration;
import com.example.Excermol.entity.dtos.IntegrationCreateRequestDTO;
import com.example.Excermol.entity.dtos.IntegrationResponseDTO;
import com.example.Excermol.entity.dtos.IntegrationUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class IntegrationMapper {

    // CreateRequestDTO → Entity
    public Integration toEntity(IntegrationCreateRequestDTO dto) {
        Integration integration = new Integration();
        integration.setIntegrationType(dto.getIntegrationType());
        integration.setDescription(dto.getDescription());
        integration.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : false);
        // workspace Service-də set olunacaq
        return integration;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(Integration integration, IntegrationUpdateRequestDTO dto) {
        if (dto.getDescription() != null) {
            integration.setDescription(dto.getDescription());
        }
        if (dto.getIsActive() != null) {
            integration.setIsActive(dto.getIsActive());
        }
    }

    // Entity → ResponseDTO
    public IntegrationResponseDTO toResponseDTO(Integration integration) {
        IntegrationResponseDTO dto = new IntegrationResponseDTO();
        dto.setId(integration.getId());
        dto.setIntegrationType(integration.getIntegrationType());
        dto.setDescription(integration.getDescription());
        dto.setIsActive(integration.getIsActive());
        dto.setCreatedAt(integration.getCreatedAt());
        dto.setUpdatedAt(integration.getUpdatedAt());

        // workspace
        if (integration.getWorkspace() != null) {
            dto.setWorkspaceId(integration.getWorkspace().getId());
        }

        return dto;
    }
}