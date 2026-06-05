package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.AttributeType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyAttributeUpdateRequestDTO {

    private String name;
    private AttributeType attributeType;
    // attributeProperty update edilmir - SYSTEM olanlar dəyişdirilə bilməz
}