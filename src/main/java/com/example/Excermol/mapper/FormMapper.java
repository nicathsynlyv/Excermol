package com.example.Excermol.mapper;

import com.example.Excermol.entity.Form;
import com.example.Excermol.entity.dtos.FormCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormResponseDTO;
import com.example.Excermol.entity.dtos.FormUpdateRequestDTO;
import com.example.Excermol.enums.FormStatus;
import org.springframework.stereotype.Component;

@Component
public class FormMapper {
    // CreateRequestDTO → Entity
    public Form toEntity(FormCreateRequestDTO dto) {
        Form form = new Form();
        form.setFormsName(dto.getFormsName());
        form.setStatus(FormStatus.DRAFT);        // yeni form həmişə DRAFT başlayır
        form.setResponsesCount(0);
        return form;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(Form form, FormUpdateRequestDTO dto) {
        if (dto.getFormsName() != null) {
            form.setFormsName(dto.getFormsName());
        }
        if (dto.getStatus() != null) {
            form.setStatus(dto.getStatus());
        }
        if (dto.getLinks() != null) {
            form.setLinks(dto.getLinks());
        }
    }

    // Entity → ResponseDTO
    public FormResponseDTO toResponseDTO(Form form) {
        FormResponseDTO dto = new FormResponseDTO();
        dto.setId(form.getId());
        dto.setFormsName(form.getFormsName());
        dto.setResponsesCount(form.getResponsesCount());
        dto.setLinks(form.getLinks());
        dto.setStatus(form.getStatus());
        dto.setCreatedAt(form.getCreatedAt());
        dto.setUpdatedAt(form.getUpdatedAt());

        // owner
        if (form.getOwner() != null) {
            dto.setOwnerId(form.getOwner().getId());
            dto.setOwnerName(form.getOwner().getFullName());
        }

        return dto;
    }
}
