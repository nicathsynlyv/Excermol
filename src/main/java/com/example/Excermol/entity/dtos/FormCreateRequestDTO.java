package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormCreateRequestDTO {
    @NotBlank(message = "Form Adı boş ola bilməz")
    @Size(min = 2,max = 100,message = "Form adı 2-100 simvol arasında olmalıdır")
    private String formsName;

}
