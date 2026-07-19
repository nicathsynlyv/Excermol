package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshTokenRequestDTO {
    @NotBlank(message = "Refresh token boş ola bilməz")
    private String refreshToken;
}
