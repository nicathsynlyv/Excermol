package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormAnswerRequestDTO {

    private Long formFieldId;
    private String value;
}
