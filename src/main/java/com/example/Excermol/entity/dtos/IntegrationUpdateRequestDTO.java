package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationUpdateRequestDTO {

    private String description;
    private Boolean isActive;  // toggle üçün
}
