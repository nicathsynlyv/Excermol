package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "Ad boş ola bilməz")
    @Size(min = 2, max = 50)
    private String fullName;

    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email boş ola bilməz")
    private String email;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 6)
    private String password;

    private UserStatus status;

    private UserRole role;
}