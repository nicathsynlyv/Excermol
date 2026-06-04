package com.example.Excermol.mapper;

import com.example.Excermol.entity.FormResponseAnswer;
import com.example.Excermol.entity.dtos.FormAnswerRequestDTO;
import com.example.Excermol.entity.dtos.FormAnswerResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class FormAnswerMapper {
    // AnswerRequestDTO → Entity
    public  FormResponseAnswer toEntity(FormAnswerRequestDTO dto) {
        FormResponseAnswer answer = new FormResponseAnswer();
        answer.setValue(dto.getValue());
        // formField və formResponse Service-də set olunacaq
        return answer;
    }

    // Entity → AnswerResponseDTO
    public  FormAnswerResponseDTO toResponseDTO(FormResponseAnswer answer) {
        FormAnswerResponseDTO dto = new FormAnswerResponseDTO();
        dto.setId(answer.getId());
        dto.setValue(answer.getValue());

        // formField
        if (answer.getFormField() != null) {
            dto.setFormFieldId(answer.getFormField().getId());
            dto.setFieldLabel(answer.getFormField().getLabel());
        }

        return dto;
    }
}
