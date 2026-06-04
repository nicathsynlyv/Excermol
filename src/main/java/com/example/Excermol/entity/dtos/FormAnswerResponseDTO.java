package com.example.Excermol.entity.dtos;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormAnswerResponseDTO {

    private Long id;
    private Long formFieldId;
    private String fieldLabel;
    private String value;
}