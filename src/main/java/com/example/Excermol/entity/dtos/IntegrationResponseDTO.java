package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.IntegrationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class IntegrationResponseDTO {

    private Long id;
    private IntegrationType integrationType;
    private String description;
    private Boolean isActive;

    // workspace
    private Long workspaceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
