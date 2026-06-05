package com.example.Excermol.mapper;

import com.example.Excermol.entity.CompanyAttribute;
import com.example.Excermol.entity.dtos.CompanyAttributeCreateRequestDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeResponseDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeUpdateRequestDTO;
import com.example.Excermol.enums.AttributeProperty;
import org.springframework.stereotype.Component;

@Component
public class CompanyAttributeMapper {

    // CreateRequestDTO → Entity
    public CompanyAttribute toEntity(CompanyAttributeCreateRequestDTO dto) {
        CompanyAttribute attribute = new CompanyAttribute();
        attribute.setName(dto.getName());
        attribute.setAttributeType(dto.getAttributeType());
        attribute.setAttributeProperty(dto.getAttributeProperty() != null
                ? dto.getAttributeProperty()
                : AttributeProperty.CUSTOM); // default CUSTOM
        // workspace Service-də set olunacaq
        return attribute;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(CompanyAttribute attribute, CompanyAttributeUpdateRequestDTO dto) {
        if (dto.getName() != null) {
            attribute.setName(dto.getName());
        }
        if (dto.getAttributeType() != null) {
            attribute.setAttributeType(dto.getAttributeType());
        }
    }

    // Entity → ResponseDTO
    public CompanyAttributeResponseDTO toResponseDTO(CompanyAttribute attribute) {
        CompanyAttributeResponseDTO dto = new CompanyAttributeResponseDTO();
        dto.setId(attribute.getId());
        dto.setName(attribute.getName());
        dto.setAttributeType(attribute.getAttributeType());
        dto.setAttributeProperty(attribute.getAttributeProperty());
        dto.setCreatedAt(attribute.getCreatedAt());
        dto.setUpdatedAt(attribute.getUpdatedAt());

        // workspace
        if (attribute.getWorkspace() != null) {
            dto.setWorkspaceId(attribute.getWorkspace().getId());
        }

        return dto;
    }
}
