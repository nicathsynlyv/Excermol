package com.example.Excermol.entity.dtos;


import com.example.Excermol.enums.PhoneCountryCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
public class WorkspaceResponseDTO {
    private Long id;
    private String name;
    private String workspaceAvatar;
    private String currency;
    private PhoneCountryCode phoneCountryCode;

    // owner
    private Long ownerId;
    private String ownerName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
