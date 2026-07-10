package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.CompanyStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CompanyRequestDTO {

    // Create Company popup-dan — məcburi fieldlər
    @NotBlank(message = "Şirkət adı boş ola bilməz")
    @Size(min = 2, max = 150,message = "Şirkət adı 2-150 simvol arası olmalıdır")
    private String companyName;

    @NotBlank(message = "Domain boş ola bilməz")
    @Size(max = 255,message = "Domain 255 simvoldan çox ola bilməz")
    @Pattern(
            regexp = "^(https?://)?(www\\.)?([\\w-]+\\.)+[\\w-]{2,4}(/.*)?$",
            message = "Düzgün domain daxil edin (məs: example.com)"
    )
    private String domain;

    // Optional fieldlər
    @Email(message = "Email formatı düzgün deyil")
    @Size(max = 150,message = "Email 150 simvoldan cox ola bilməz")
    private String emailAddress;

    @Size(max = 100,message = "Lead mənbəyi 100 simvoldan çox ola bilməz")
    private String leadSource;

    @Size(max = 100,message = "Şəhər 100 simvoldan çox ola bilməz")
    private String city;
    @Size(max = 100, message = "Connection 100 simvoldan çox ola bilməz")
    private String connection;

    @DecimalMin(value = "0.0", message = "Lead dəyəri mənfi ola bilməz")
    @DecimalMax(value = "999999999.99", message = "Lead dəyəri həddən artıq böyükdür")
    private BigDecimal leadValue;

    private CompanyStatus status;

    // Owner — şirkətin sahibi
    private Long ownerId;



    // User ← new changes
    @NotNull(message = "User boş ola bilməz")
    private Long userId;
}