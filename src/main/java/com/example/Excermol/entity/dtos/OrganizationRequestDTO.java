package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.OrganizationType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRequestDTO {
    @NotBlank(message = "Organization adı boş ola bilməz")
    private String name;

    @Pattern(
            regexp = "^(https?:\\/\\/)?([\\w-]+\\.)+[\\w-]{2,4}$",
            message = "Düzgün domain daxil edin"
    )    private String domain;

    @Size(max = 250, message = "Təsvir 250 simvoldan çox ola bilməz")
    private String description;

    private Long managerId; // User id göndəririk

    private OrganizationType listName;

    private Integer numberOfDeals;

    private Integer employeesRange;
}
