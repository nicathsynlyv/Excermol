package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FieldType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormFieldCreateRequestDTO {


    @NotBlank(message = "Label boş ola bilməz")
    @Size(min = 2, max = 100, message = "Label 2-100 simvol arasinda olmalidir")
    private String label;         // "Work Email Address"

    @Size(max = 200, message = "Placeholder 200 simvoldan çox ola bilməz")
    private String placeholder;   // "Enter email address"

    @NotNull(message = "Field tipi boş ola bilməz")
    private FieldType fieldType;  // EMAIL, TEXT, SELECT...

    private Boolean isRequired;

    private Integer fieldOrder;   // sahənin sırası

    @NotNull(message = "Form ID boş ola bilməz")
    private Long formId;          // hansı form-a aiddir
}
