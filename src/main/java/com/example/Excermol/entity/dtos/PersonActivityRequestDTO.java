package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.ActivityAction;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonActivityRequestDTO {
    @NotNull(message = "Action boş ola bilməz")
    private ActivityAction action;

    private String performedBy;  // "Anwar Hussen", "Tahsan Khan"

    @NotNull(message = "Person ID boş ola bilməz")
    private Long personId;
}
