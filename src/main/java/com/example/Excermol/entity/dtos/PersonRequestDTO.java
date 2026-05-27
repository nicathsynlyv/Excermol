package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.ConnectionStrength;
import com.example.Excermol.enums.PersonStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class PersonRequestDTO {
    // Create People popup-dan — məcburi fieldlər
    @NotBlank(message = "Ad boş ola bilməz")
    private String fullName;

    @NotBlank(message = "Soyad boş ola bilməz")
    private String lastName;

    @Email(message = "Düzgün email daxil edin")
    @NotBlank(message = "Email boş ola bilməz")
    private String email;

    @NotNull(message = "Status boş ola bilməz")
    private PersonStatus status;

    // Details səhifəsindən — optional fieldlər
    private String jobTitle;
    private String websiteUrl;
    private String phone;
    private String linkedinUrl;
    private String whatsappUsername;
    private String twitterName;
    private String instagramName;
    private BigDecimal leadValue;
    private String lists;
    private ConnectionStrength connectionStrength;


    private Long companyId;
    private Set<Long> tagIds;
}
