package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequestDTO {

    @NotBlank(message = "Email boş ola bilməz")
    @Email(message = "Email formatı düzgün deyil")
    private String email;

    @NotBlank(message = "Kod boş ola bilməz")
    @Size(min = 4, max = 4, message = "Kod 4 rəqəmdən ibarət olmalıdır")
    private String otpCode;
}