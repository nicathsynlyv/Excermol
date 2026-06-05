package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.IntegrationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationCreateRequestDTO {

    private IntegrationType integrationType;
    private String description;
    private Boolean isActive;
    private Long workspaceId;
}