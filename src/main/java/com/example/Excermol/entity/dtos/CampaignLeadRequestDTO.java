package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.LeadStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CampaignLeadRequestDTO {

    @NotBlank(message = "Lead adı boş ola bilməz")
    @Size(min = 2, max = 100, message = "Ad 2-100 simvol arası olmalıdır")
    private String leadName;

    @NotBlank(message = "Lead email boş ola bilməz")
    @Email(message = "Email formatı düzgün deyil")
    @Size(max = 100, message = "Email 100 simvoldan çox ola bilməz")
    private String leadEmail;

    private Long companyId;

    private Long campaignId;

    private LocalDate date;

    @Min(value = 0, message = "Progress mənfi ola bilməz")
    @Max(value = 100, message = "Progress 100-dən çox ola bilməz")
    private Integer progress;

    @NotNull(message = "Status boş ola bilməz")
    private LeadStatus status;
}
