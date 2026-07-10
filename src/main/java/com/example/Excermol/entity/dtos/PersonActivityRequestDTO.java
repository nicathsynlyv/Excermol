package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.ActivityAction;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonActivityRequestDTO {
    @NotNull(message = "Action boş ola bilməz")
    private ActivityAction action;

    @Size(max = 100,message = "PerformedBy 100 simvoldan çox ola bilməz")
    private String performedBy;  // "Anwar Hussen", "Tahsan Khan"

    @NotNull(message = "Person ID boş ola bilməz")
    @Positive(message = "Person ID müsbət olmalıdır")
    private Long personId;
}
