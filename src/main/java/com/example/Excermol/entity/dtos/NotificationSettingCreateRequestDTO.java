package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingCreateRequestDTO {

    @NotNull(message = "Workspace ID boş ola bilməz")
    private Long workspaceId;
    @NotNull(message = "User ID boş ola bilməz")
    private Long userId;
    private Boolean anyNewComment;
    private Boolean assignedToOrganization;
    private Boolean followingOrganization;
}
