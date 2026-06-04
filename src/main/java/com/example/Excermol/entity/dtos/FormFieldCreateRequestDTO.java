package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FieldType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormFieldCreateRequestDTO {

    private String label;         // "Work Email Address"
    private String placeholder;   // "Enter email address"
    private FieldType fieldType;  // EMAIL, TEXT, SELECT...
    private Boolean isRequired;
    private Integer fieldOrder;   // sahənin sırası
    private Long formId;          // hansı form-a aiddir
}
