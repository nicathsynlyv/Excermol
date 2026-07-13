package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.AttributeType;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyAttributeUpdateRequestDTO {

    @Size(min = 2, max = 100,message = "Attribute adı 2-100 simvol arasında olmalıdır")
    private String name;
    private AttributeType attributeType;
    // attributeProperty update edilmir - SYSTEM olanlar dəyişdirilə bilməz
}