package com.example.Excermol.entity.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//FormResponse entiysine uygun
public class FormSubmitRequestDTO {

    @NotNull(message = "FormId boş ola bilməz")
    private Long formId;

    private Long contactId;

    @NotEmpty(message = "Ən azı bir cavab olmalıdır")
    @Valid
    private List<FormAnswerRequestDTO> answers;

}