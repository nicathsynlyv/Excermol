package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.IntegrationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntegrationCreateRequestDTO {

    @NotNull(message = "Integration tipi boş ola bilməz")
    private IntegrationType integrationType;

    @Size(max = 500,message = "Təsvir 500 simvoldan çox ola bilməz")
    private String description;

    private Boolean isActive;

    @NotNull(message = "Workspace ID boş ola bilməz")
    private Long workspaceId;
}