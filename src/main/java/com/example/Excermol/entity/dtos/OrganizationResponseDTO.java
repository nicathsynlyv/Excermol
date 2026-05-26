package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.OrganizationType;

import lombok.Getter;

import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrganizationResponseDTO {

    private Long id;
    private String name;
    private String domain;
    private String description;

    private Long managerId;       // UI-da manager seçimi üçün
    private String managerName;   // UI-da göstərmək üçün

    private LocalDateTime createdAt;
    private LocalDateTime lastInteractedAt;

    private OrganizationType listName;
    private Integer numberOfDeals;
    private Integer employeesRange;
}
