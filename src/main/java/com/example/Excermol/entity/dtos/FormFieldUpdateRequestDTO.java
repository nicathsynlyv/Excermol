package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormFieldUpdateRequestDTO {

    private String label;
    private String placeholder;
    private FieldType fieldType;
    private Boolean isRequired;
    private Integer fieldOrder;
}