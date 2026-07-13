package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.AttributeProperty;
import com.example.Excermol.enums.AttributeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyAttributeCreateRequestDTO {

    @NotBlank(message = "Attribute adı boş ola bilməz")
    @Size(min = 2, max = 100, message = "Attribute adı 2-100 simvol arasında olmalıdır")
    private String name;

    // "LinkedIn Company URL"
    @NotNull(message = "Attribute tipi boş ola bilməz")
    private AttributeType attributeType;        // LINKEDIN, TEXT, EMAIL...

    @NotNull(message = "Attribute property boş ola bilməz")
    private AttributeProperty attributeProperty; // SYSTEM, CUSTOM

    @NotNull(message = "Workspace ID boş ola bilməz")
    private Long workspaceId;
}
