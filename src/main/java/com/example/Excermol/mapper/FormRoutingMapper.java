package com.example.Excermol.mapper;

import com.example.Excermol.entity.FormRouting;
import com.example.Excermol.entity.dtos.FormRoutingCreateRequestDTO;
import com.example.Excermol.entity.dtos.FormRoutingResponseDTO;
import com.example.Excermol.entity.dtos.FormRoutingUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class FormRoutingMapper {
    // CreateRequestDTO → Entity
    public FormRouting toEntity(FormRoutingCreateRequestDTO dto) {
        FormRouting formRouting = new FormRouting();
        formRouting.setConditionType(dto.getConditionType());
        formRouting.setConditionValue(dto.getConditionValue());
        formRouting.setRedirectTo(dto.getRedirectTo());
        formRouting.setRoutingOrder(dto.getRoutingOrder());
        // form, formField, email Service-də set olunacaq
        return formRouting;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public  void updateEntity(FormRouting formRouting, FormRoutingUpdateRequestDTO dto) {
        if (dto.getConditionType() != null) {
            formRouting.setConditionType(dto.getConditionType());
        }
        if (dto.getConditionValue() != null) {
            formRouting.setConditionValue(dto.getConditionValue());
        }
        if (dto.getRedirectTo() != null) {
            formRouting.setRedirectTo(dto.getRedirectTo());
        }
        if (dto.getRoutingOrder() != null) {
            formRouting.setRoutingOrder(dto.getRoutingOrder());
        }
        // formField və email Service-də set olunacaq
    }

    // Entity → ResponseDTO
    public FormRoutingResponseDTO toResponseDTO(FormRouting formRouting) {
        FormRoutingResponseDTO dto = new FormRoutingResponseDTO();
        dto.setId(formRouting.getId());
        dto.setConditionType(formRouting.getConditionType());
        dto.setConditionValue(formRouting.getConditionValue());
        dto.setRedirectTo(formRouting.getRedirectTo());
        dto.setRoutingOrder(formRouting.getRoutingOrder());
        dto.setCreatedAt(formRouting.getCreatedAt());
        dto.setUpdatedAt(formRouting.getUpdatedAt());

        // form
        if (formRouting.getForm() != null) {
            dto.setFormId(formRouting.getForm().getId());
        }

        // formField
        if (formRouting.getFormField() != null) {
            dto.setFormFieldId(formRouting.getFormField().getId());
            dto.setFormFieldLabel(formRouting.getFormField().getLabel());
        }

        // email
        if (formRouting.getEmail() != null) {
            dto.setEmailId(formRouting.getEmail().getId());
            dto.setEmailSubject(formRouting.getEmail().getSubject());
        }

        return dto;
    }
}