package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyAttributeCreateRequestDTO {

    private String name;                        // "LinkedIn Company URL"
    private AttributeType attributeType;        // LINKEDIN, TEXT, EMAIL...
    private AttributeProperty attributeProperty; // SYSTEM, CUSTOM
    private Long workspaceId;
}
