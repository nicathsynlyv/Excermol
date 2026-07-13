package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationUpdateRequestDTO {

    @Size(max = 500 ,message = "Təsvir 500 simvoldan çox ola bilməz")
    private String description;

    private Boolean isActive;  // toggle üçün
}
