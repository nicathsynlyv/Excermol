package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.CampaignStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignRequestDTO {
    @NotBlank(message = "Kampaniya adı boş ola bilməz")
    private String name;

    private CampaignStatus status;


    // User ← new changes
    @NotNull(message = "User boş ola bilməz")
    private Long userId;
}
