package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.NotificationSettingService;
import com.example.Excermol.entity.NotificationSetting;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.Workspace;
import com.example.Excermol.entity.dtos.NotificationSettingCreateRequestDTO;
import com.example.Excermol.entity.dtos.NotificationSettingResponseDTO;
import com.example.Excermol.entity.dtos.NotificationSettingUpdateRequestDTO;
import com.example.Excermol.exception.NotificationSettingAlreadyExistsException;
import com.example.Excermol.exception.NotificationSettingNotFoundException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.exception.WorkspaceNotFoundException;
import com.example.Excermol.mapper.NotificationSettingMapper;
import com.example.Excermol.repository.NotificationSettingRepository;
import com.example.Excermol.repository.UserRepository;
import com.example.Excermol.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class NotificationSettingServiceImpl implements NotificationSettingService {

    private final NotificationSettingRepository notificationSettingRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final NotificationSettingMapper notificationSettingMapper;

    public NotificationSettingServiceImpl(NotificationSettingRepository notificationSettingRepository,
                                          WorkspaceRepository workspaceRepository,
                                          UserRepository userRepository,
                                          NotificationSettingMapper notificationSettingMapper) {
        this.notificationSettingRepository = notificationSettingRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.notificationSettingMapper = notificationSettingMapper;
    }

    @Override
    public NotificationSettingResponseDTO createNotificationSetting(NotificationSettingCreateRequestDTO dto) {
        // user-in bu workspace-də artıq setting-i varmı?
        if (notificationSettingRepository.existsByWorkspaceIdAndUserId(
                dto.getWorkspaceId(), dto.getUserId())) {
            throw new NotificationSettingAlreadyExistsException("Bu user üçün artıq notification setting mövcuddur");
        }

        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + dto.getWorkspaceId()));

        // user tap
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: " + dto.getUserId()));

        // setting yarat
        NotificationSetting setting = notificationSettingMapper.toEntity(dto);
        setting.setWorkspace(workspace);
        setting.setUser(user);

        NotificationSetting saved = notificationSettingRepository.save(setting);
        return notificationSettingMapper.toResponseDTO(saved);
    }

    @Override
    public List<NotificationSettingResponseDTO> getSettingsByWorkspaceId(Long workspaceId) {
        return notificationSettingRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(notificationSettingMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationSettingResponseDTO> getSettingsByUserId(Long userId) {
        return notificationSettingRepository.findAllByUserId(userId)
                .stream()
                .map(notificationSettingMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public NotificationSettingResponseDTO getSettingByWorkspaceAndUser(Long workspaceId, Long userId) {
        return notificationSettingRepository.findAllByWorkspaceIdAndUserId(workspaceId, userId)
                .stream()
                .findFirst()
                .map(notificationSettingMapper::toResponseDTO)
                .orElseThrow(() -> new NotificationSettingNotFoundException(
                        "Notification setting tapılmadı: workspaceId=" + workspaceId + ", userId=" + userId));
    }

    @Override
    public NotificationSettingResponseDTO getSettingById(Long id) {
        NotificationSetting setting = notificationSettingRepository.findById(id)
                .orElseThrow(() -> new NotificationSettingNotFoundException("Notification setting tapılmadı: " + id));
        return notificationSettingMapper.toResponseDTO(setting);
    }

    @Override
    public NotificationSettingResponseDTO updateSetting(Long id, NotificationSettingUpdateRequestDTO dto) {
        NotificationSetting setting = notificationSettingRepository.findById(id)
                .orElseThrow(() -> new NotificationSettingNotFoundException("Notification setting tapılmadı: " + id));

        notificationSettingMapper.updateEntity(setting, dto);

        NotificationSetting updated = notificationSettingRepository.save(setting);
        return notificationSettingMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteSetting(Long id) {
        NotificationSetting setting = notificationSettingRepository.findById(id)
                .orElseThrow(() -> new NotificationSettingNotFoundException("Notification setting tapılmadı: " + id));
        notificationSettingRepository.delete(setting);
    }
}
