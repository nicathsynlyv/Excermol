package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
//FormResponse entiysine uygun
public class FormSubmitRequestDTO {

    private Long formId;
    private Long contactId;
    private List<FormAnswerRequestDTO> answers;
}