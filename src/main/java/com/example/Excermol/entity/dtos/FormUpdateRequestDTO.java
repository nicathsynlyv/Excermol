package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FormStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormUpdateRequestDTO {
    private String formsName;
    private FormStatus status;
    private String links;
}
