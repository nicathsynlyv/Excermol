package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationSettingResponseDTO {

    private Long id;

    // workspace
    private Long workspaceId;

    // user
    private Long userId;
    private String userName;

    // checkboxlar
    private Boolean anyNewComment;
    private Boolean assignedToOrganization;
    private Boolean followingOrganization;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}