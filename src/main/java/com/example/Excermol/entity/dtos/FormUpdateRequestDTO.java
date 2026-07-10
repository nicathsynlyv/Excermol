package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FormStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormUpdateRequestDTO {
    @Size(min = 2, max = 100, message = "Form adı 2-100 simvol arasında olmalıdır")
    private String formsName;

    private FormStatus status;
    @Size(max = 500,message = "Link 500 simvoldan çox ola bilməz")
    private String links;
}
