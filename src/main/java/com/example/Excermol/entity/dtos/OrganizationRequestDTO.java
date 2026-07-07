package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.OrganizationType;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationRequestDTO {

    @NotBlank(message = "Organization adı boş ola bilməz")
    @Size(min = 2,max = 100,message = "Ad 2-100 simvol arasında olmalıdır")
    private String name;

    @NotBlank(message = "Domain boş ola bilməz")
    @Pattern(
            regexp = "^(https?://)?(www\\.)?([\\w-]+\\.)+[\\w-]{2,4}(/.*)?$",
            message = "Düzgün domain daxil edin (məs: example.com) "
    )    private String domain;

    @Size(max = 250, message = "Təsvir 250 simvoldan çox ola bilməz")
    private String description;


    @Positive(message = "Menager id müsbət  olmalidir")
    private Long managerId; // User id göndəririk

    private OrganizationType listName;

    @PositiveOrZero(message = "Deal sayı mənfi ola bilməz")
    private Integer numberOfDeals;

    @PositiveOrZero(message = "İşçi sayı mənfi ola bilməz")
    private Integer employeesRange;
}
