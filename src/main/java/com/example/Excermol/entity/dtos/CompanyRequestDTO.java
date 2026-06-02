package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.CompanyStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CompanyRequestDTO {

    // Create Company popup-dan — məcburi fieldlər
    @NotBlank(message = "Şirkət adı boş ola bilməz")
    private String companyName;

    @NotBlank(message = "Domain boş ola bilməz")
    private String domain;

    // Optional fieldlər
    @Email
    private String emailAddress;
    private String leadSource;
    private String city;
    private String connection;
    private BigDecimal leadValue;
    private CompanyStatus status;

    // Owner — şirkətin sahibi
    private Long ownerId;



    // User ← new changes
    @NotNull(message = "User boş ola bilməz")
    private Long userId;
}