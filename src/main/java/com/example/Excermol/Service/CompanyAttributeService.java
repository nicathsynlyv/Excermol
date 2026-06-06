package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.CompanyAttributeCreateRequestDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeResponseDTO;
import com.example.Excermol.entity.dtos.CompanyAttributeUpdateRequestDTO;
import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;

import java.util.List;

public interface CompanyAttributeService {

    // attribute yarat - "Create attribute" buttonu
    CompanyAttributeResponseDTO createAttribute(CompanyAttributeCreateRequestDTO dto);

    // workspace-ə görə bütün attribute-lar
    List<CompanyAttributeResponseDTO> getAttributesByWorkspaceId(Long workspaceId);

    // id-yə görə attribute
    CompanyAttributeResponseDTO getAttributeById(Long id);

    // property-ə görə - SYSTEM və ya CUSTOM
    List<CompanyAttributeResponseDTO> getAttributesByProperty(Long workspaceId, AttributeProperty property);

    // tipə görə - EMAIL, TEXT, LINKEDIN...
    List<CompanyAttributeResponseDTO> getAttributesByType(Long workspaceId, AttributeType type);

    // ada görə axtarış
    List<CompanyAttributeResponseDTO> searchAttributesByName(Long workspaceId, String name);

    // attribute update et - yalnız CUSTOM olanlar
    CompanyAttributeResponseDTO updateAttribute(Long id, CompanyAttributeUpdateRequestDTO dto);

    // attribute sil - yalnız CUSTOM olanlar
    void deleteAttribute(Long id);
}
