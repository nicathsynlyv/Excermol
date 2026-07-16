package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    @NotBlank(message = "Ad boş ola bilməz")
    @Size(min = 2, max = 50, message = "Ad 2-50 simvol arasında olmalıdır")
    private String fullName;

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Email formatı düzgün deyil")
    private String email;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 6, message = "Şifrə minimum 6 simvol olmalıdır")
    private String password;

}
