package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.UserRole;
import com.example.Excermol.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "Ad boş ola bilməz")
    @Size(min = 2, max = 50,message = "Ad 2-50 simvol arası olmalıdlır")
    private String fullName;

    @Email(message = "Email formatı düzgün deyil")
    @NotBlank(message = "Email boş ola bilməz")
    @Size(max = 100,message = "Email 100 simvoldan cox ola bilməz")
    private String email;

    @NotBlank(message = "Şifrə boş ola bilməz")
    @Size(min = 6,max = 50, message = "Şifrə 6-50 simvol arası olmalıdır")
    private String password;

    @NotNull(message = "Status boş ola bilməz")
    private UserStatus status;

    @NotNull(message = "Role boş ola bilməz")
    private UserRole role;
}