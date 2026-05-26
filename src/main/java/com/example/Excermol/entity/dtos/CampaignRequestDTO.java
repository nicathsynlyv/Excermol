package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.CampaignStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignRequestDTO {
    @NotBlank(message = "Kampaniya adı boş ola bilməz")
    private String name;

    private CampaignStatus status;
}
