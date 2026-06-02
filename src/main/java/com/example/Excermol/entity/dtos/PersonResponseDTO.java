package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.ConnectionStrength;
import com.example.Excermol.enums.PersonStatus;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class PersonResponseDTO {
    private Long id;
    private String fullName;
    private String lastName;
    private String email;
    private String jobTitle;
    private String websiteUrl;
    private String phone;
    private String linkedinUrl;
    private String whatsappUsername;
    private String twitterName;
    private String instagramName;
    private BigDecimal leadValue;
    private String lists;
    private ConnectionStrength connectionStrength;
    private PersonStatus status;
    private LocalDateTime lastInteractionAt;

    // Company
    private Long companyId;
    private String companyName;   // UI-da şirkət adı görünür

    // Tags — UI-da "Agency", "Startup", "Scale Up"
    private Set<String> tagNames;

//new changes
    private Long userId;

}
