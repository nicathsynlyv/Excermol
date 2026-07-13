package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagCreateRequestDTO {

    @NotBlank(message = "Tag adı boş ola bilməz")
    @Size(min = 2, max = 50, message = "Tag adı 2-50 simvol arasında olmalıdır")
    private String name;   // "Agency", "Scale Up"

    @Pattern(
            regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$",
            message = "Düzgün rəng kodu daxil edin (məs: #FF0000)")
    private String color;  // "#FF0000"
}

