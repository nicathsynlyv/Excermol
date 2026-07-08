package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.CampaignStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CampaignRequestDTO {


    @NotBlank(message = "Kampaniya adı boş ola bilməz")
    @Size(min = 2,max = 100,message = "Ad 2-100 simvol arasi olmalidir")
    private String name;


    @NotNull(message = "Status boş ola bilməz")
    private CampaignStatus status;


    // User ← new changes
    @NotNull(message = "User boş ola bilməz")
    private Long userId;
}
