package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.NotificationSettingCreateRequestDTO;
import com.example.Excermol.entity.dtos.NotificationSettingResponseDTO;
import com.example.Excermol.entity.dtos.NotificationSettingUpdateRequestDTO;

import java.util.List;

public interface NotificationSettingService {
    // notification setting yarat
    NotificationSettingResponseDTO createNotificationSetting(NotificationSettingCreateRequestDTO dto);

    // workspace-ə görə bütün settings
    List<NotificationSettingResponseDTO> getSettingsByWorkspaceId(Long workspaceId);

    // user-ə görə bütün settings
    List<NotificationSettingResponseDTO> getSettingsByUserId(Long userId);

    // workspace və user-ə görə setting tap
    NotificationSettingResponseDTO getSettingByWorkspaceAndUser(Long workspaceId, Long userId);

    // id-yə görə setting
    NotificationSettingResponseDTO getSettingById(Long id);

    // checkboxları update et
    NotificationSettingResponseDTO updateSetting(Long id, NotificationSettingUpdateRequestDTO dto);

    // setting sil
    void deleteSetting(Long id);
}
