package com.example.Excermol.mapper;

import com.example.Excermol.entity.FormResponse;
import com.example.Excermol.entity.dtos.FormAnswerResponseDTO;
import com.example.Excermol.entity.dtos.FormSubmitRequestDTO;
import com.example.Excermol.entity.dtos.FormSubmitResponseDTO;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class FormResponseMapper {

    private final FormAnswerMapper formAnswerMapper;

    public FormResponseMapper(FormAnswerMapper formAnswerMapper) {
        this.formAnswerMapper = formAnswerMapper;
    }

    // SubmitRequestDTO → Entity
    public FormResponse toEntity(FormSubmitRequestDTO dto) {
        FormResponse formResponse = new FormResponse();
        // form və contact Service-də set olunacaq
        return formResponse;
    }

    // Entity → SubmitResponseDTO
    public FormSubmitResponseDTO toResponseDTO(FormResponse formResponse) {
        FormSubmitResponseDTO dto = new FormSubmitResponseDTO();
        dto.setId(formResponse.getId());
        dto.setSubmittedAt(formResponse.getSubmittedAt());
        dto.setCreatedAt(formResponse.getCreatedAt());
        dto.setUpdatedAt(formResponse.getUpdatedAt());

        // form
        if (formResponse.getForm() != null) {
            dto.setFormId(formResponse.getForm().getId());
        }

        // contact
        if (formResponse.getContact() != null) {
            dto.setContactId(formResponse.getContact().getId());
            dto.setContactFirstName(formResponse.getContact().getFullName());
            dto.setContactLastName(formResponse.getContact().getLastName());
            dto.setContactEmail(formResponse.getContact().getEmail());
        }

        // answers
        if (formResponse.getAnswers() != null) {
            List<FormAnswerResponseDTO> answerDtos = formResponse.getAnswers()
                    .stream()
                    .map(formAnswerMapper::toResponseDTO)
                    .collect(Collectors.toList());
            dto.setAnswers(answerDtos);
        } else {
            dto.setAnswers(new ArrayList<>());
        }

        return dto;
    }
}
