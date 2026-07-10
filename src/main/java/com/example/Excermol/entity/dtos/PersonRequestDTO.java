package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.ConnectionStrength;
import com.example.Excermol.enums.PersonStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
public class PersonRequestDTO {
    // Create People popup-dan — məcburi fieldlər
    @NotBlank(message = "Ad boş ola bilməz")
    @Size(min = 2, max = 50, message = "Ad 2-50 simvol arasi olmalidir")
    private String fullName;

    @NotBlank(message = "Soyad boş ola bilməz")
    @Size(min = 2, max = 50, message = "Soyad 2-50 simvol arası olmalıdır")
    private String lastName;


    @Email(message = "Düzgün email daxil edin")
    @NotBlank(message = "Email boş ola bilməz")
    @Size(max = 100, message = "Email 100 simvoldan cox ola bilməz")
    private String email;

    @NotNull(message = "Status boş ola bilməz")
    private PersonStatus status;


    // Details səhifəsindən — optional fieldlər
    @Size(max = 100, message = "Vəzifə 100 simvoldan çox ola bilməz")
    private String jobTitle;
    @Size(max = 255, message = "Website URL 255 simvoldan çox ola bilməz")
    private String websiteUrl;

    private String phone;

    @Size(max = 255, message = "LinkedIn URL 255 simvoldan çox ola bilməz")
    private String linkedinUrl;

    @Size(max = 50, message = "WhatsApp username 50 simvoldan çox ola bilməz")
    private String whatsappUsername;

    @Size(max = 50, message = "Twitter adı 50 simvoldan çox ola bilməz")
    private String twitterName;
    @Size(max = 50, message = "Instagram adı 50 simvoldan çox ola bilməz")
    private String instagramName;
    @DecimalMin(value = "0.0", message = "Lead dəyəri mənfi ola bilməz")
    @DecimalMax(value = "9999999.99", message = "Lead dəyəri həddən artıq böyükdür")
    private BigDecimal leadValue;
    @Size(max = 100, message = "List adı 100 simvoldan çox ola bilməz")
    private String lists;
    private ConnectionStrength connectionStrength;


    private Long companyId;
    private Set<Long> tagIds;


    //new changes
    @NotNull(message = "User boş ola bilməz")
    private Long userId;
}
