package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormAnswerRequestDTO {

    @NotBlank(message = "FormField ID boş ola bilməz")
    private Long formFieldId;

    @Size(max = 5000,message = "5000 simvoldan çox ola bilməz")
    private String value;
}
