package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationSettingUpdateRequestDTO {

    private Boolean anyNewComment;
    private Boolean assignedToOrganization;
    private Boolean followingOrganization;
}
