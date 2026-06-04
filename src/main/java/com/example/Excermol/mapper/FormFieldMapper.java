package com.example.Excermol.mapper;

import com.example.Excermol.entity.FormField;
import com.example.Excermol.entity.dtos.FormFieldCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormFieldResponseDTO;
import com.example.Excermol.entity.dtos.FormFieldUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class FormFieldMapper {
    // CreateRequestDTO → Entity
    public  FormField toEntity(FormFieldCreateRequestDTO dto) {
        FormField formField = new FormField();
        formField.setLabel(dto.getLabel());
        formField.setPlaceholder(dto.getPlaceholder());
        formField.setFieldType(dto.getFieldType());
        formField.setIsRequired(dto.getIsRequired() != null ? dto.getIsRequired() : false);
        formField.setFieldOrder(dto.getFieldOrder());
        // form set edilmir - Service-də set olunacaq
        return formField;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public  void updateEntity(FormField formField, FormFieldUpdateRequestDTO dto) {
        if (dto.getLabel() != null) {
            formField.setLabel(dto.getLabel());
        }
        if (dto.getPlaceholder() != null) {
            formField.setPlaceholder(dto.getPlaceholder());
        }
        if (dto.getFieldType() != null) {
            formField.setFieldType(dto.getFieldType());
        }
        if (dto.getIsRequired() != null) {
            formField.setIsRequired(dto.getIsRequired());
        }
        if (dto.getFieldOrder() != null) {
            formField.setFieldOrder(dto.getFieldOrder());
        }
    }

    // Entity → ResponseDTO
    public FormFieldResponseDTO toResponseDTO(FormField formField) {
        FormFieldResponseDTO dto = new FormFieldResponseDTO();
        dto.setId(formField.getId());
        dto.setLabel(formField.getLabel());
        dto.setPlaceholder(formField.getPlaceholder());
        dto.setFieldType(formField.getFieldType());
        dto.setIsRequired(formField.getIsRequired());
        dto.setFieldOrder(formField.getFieldOrder());
        dto.setCreatedAt(formField.getCreatedAt());
        dto.setUpdatedAt(formField.getUpdatedAt());

        // form
        if (formField.getForm() != null) {
            dto.setFormId(formField.getForm().getId());
        }

        return dto;
    }
}
