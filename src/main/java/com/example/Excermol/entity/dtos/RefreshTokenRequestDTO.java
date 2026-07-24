package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//RefreshTokenRequestDTO — artıq refresh/logout-da istifadə olunmur (cookie-dən oxuyur) gelecekde istifade ucun her ehtimal saxliyiram
public class RefreshTokenRequestDTO {
    @NotBlank(message = "Refresh token boş ola bilməz")
    private String refreshToken;
}
