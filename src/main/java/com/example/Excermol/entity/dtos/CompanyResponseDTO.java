package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.CompanyStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CompanyResponseDTO {

    private Long id;
    private String companyName;
    private String domain;
    private String emailAddress;
    private String leadSource;
    private String city;
    private String connection;
    private BigDecimal leadValue;
    private CompanyStatus status;

    // Owner
    private Long ownerId;
    private String ownerName;
}
