package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FieldType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormFieldUpdateRequestDTO {

    @Size(min = 2, max = 100, message = "Label 2-100 simvol arasında olmalıdır")
    private String label;
    @Size(max = 200, message = "Placeholder 200 simvoldan çox ola bilməz")
    private String placeholder;

    private FieldType fieldType;
    private Boolean isRequired;
    private Integer fieldOrder;
}