package com.example.Excermol.mapper;

import com.example.Excermol.entity.NotificationSetting;
import com.example.Excermol.entity.dtos.NotificationSettingCreateRequestDTO;
import com.example.Excermol.entity.dtos.NotificationSettingResponseDTO;
import com.example.Excermol.entity.dtos.NotificationSettingUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class NotificationSettingMapper {

    // CreateRequestDTO → Entity
    public NotificationSetting toEntity(NotificationSettingCreateRequestDTO dto) {
        NotificationSetting setting = new NotificationSetting();
        setting.setAnyNewComment(dto.getAnyNewComment() != null ? dto.getAnyNewComment() : false);
        setting.setAssignedToOrganization(dto.getAssignedToOrganization() != null ? dto.getAssignedToOrganization() : false);
        setting.setFollowingOrganization(dto.getFollowingOrganization() != null ? dto.getFollowingOrganization() : false);
        // workspace və user Service-də set olunacaq
        return setting;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(NotificationSetting setting, NotificationSettingUpdateRequestDTO dto) {
        if (dto.getAnyNewComment() != null) {
            setting.setAnyNewComment(dto.getAnyNewComment());
        }
        if (dto.getAssignedToOrganization() != null) {
            setting.setAssignedToOrganization(dto.getAssignedToOrganization());
        }
        if (dto.getFollowingOrganization() != null) {
            setting.setFollowingOrganization(dto.getFollowingOrganization());
        }
    }

    // Entity → ResponseDTO
    public NotificationSettingResponseDTO toResponseDTO(NotificationSetting setting) {
        NotificationSettingResponseDTO dto = new NotificationSettingResponseDTO();
        dto.setId(setting.getId());
        dto.setAnyNewComment(setting.getAnyNewComment());
        dto.setAssignedToOrganization(setting.getAssignedToOrganization());
        dto.setFollowingOrganization(setting.getFollowingOrganization());
        dto.setCreatedAt(setting.getCreatedAt());
        dto.setUpdatedAt(setting.getUpdatedAt());

        // workspace
        if (setting.getWorkspace() != null) {
            dto.setWorkspaceId(setting.getWorkspace().getId());
        }

        // user
        if (setting.getUser() != null) {
            dto.setUserId(setting.getUser().getId());
            dto.setUserName(setting.getUser().getFullName());
        }

        return dto;
    }
}
