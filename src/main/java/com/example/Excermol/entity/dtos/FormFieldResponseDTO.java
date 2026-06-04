package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FieldType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FormFieldResponseDTO {

    private Long id;
    private String label;
    private String placeholder;
    private FieldType fieldType;
    private Boolean isRequired;
    private Integer fieldOrder;
    private Long formId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}