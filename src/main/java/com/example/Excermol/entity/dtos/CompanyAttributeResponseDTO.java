package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CompanyAttributeResponseDTO {

    private Long id;
    private String name;
    private AttributeType attributeType;
    private AttributeProperty attributeProperty;

    // workspace
    private Long workspaceId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}