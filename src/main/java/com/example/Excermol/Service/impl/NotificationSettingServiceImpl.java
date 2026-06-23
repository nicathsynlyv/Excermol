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
//1
    @Override
    public NotificationSettingResponseDTO createNotificationSetting(NotificationSettingCreateRequestDTO dto) {
        log.info("Creating notification setting for workspace id: {} and user id: {}", dto.getWorkspaceId(), dto.getUserId());
        // user-in bu workspace-də artıq setting-i varmı?
        if (notificationSettingRepository.existsByWorkspaceIdAndUserId(
                dto.getWorkspaceId(), dto.getUserId())) {
            log.warn("Notification setting already exists for workspace id: {} and user id: {}", dto.getWorkspaceId(), dto.getUserId());
            throw new NotificationSettingAlreadyExistsException("Bu user üçün artıq notification setting mövcuddur");
        }

        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> {
                    log.warn("Workspace not found with id: {}", dto.getWorkspaceId());
                    return new WorkspaceNotFoundException("Workspace tapılmadı: " + dto.getWorkspaceId());
                });

        // user tap
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", dto.getUserId());
                    return new UserNotFoundException("User tapılmadı: " + dto.getUserId());
                });

        // setting yarat
        NotificationSetting setting = notificationSettingMapper.toEntity(dto);
        setting.setWorkspace(workspace);
        setting.setUser(user);

        NotificationSetting saved = notificationSettingRepository.save(setting);
        log.info("Notification setting created successfully with id: {}", saved.getId());
        return notificationSettingMapper.toResponseDTO(saved);
    }
//2
    @Override
    public List<NotificationSettingResponseDTO> getSettingsByWorkspaceId(Long workspaceId) {
        log.info("Fetching notification settings for workspace id: {}", workspaceId);
        List<NotificationSettingResponseDTO> settings = notificationSettingRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(notificationSettingMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} notification settings for workspace id: {}", settings.size(), workspaceId);
        return settings;
    }
//3
    @Override
    public List<NotificationSettingResponseDTO> getSettingsByUserId(Long userId) {
        log.info("Fetching notification settings for user id: {}", userId);
        List<NotificationSettingResponseDTO> settings = notificationSettingRepository.findAllByUserId(userId)
                .stream()
                .map(notificationSettingMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.info("Retrieved {} notification settings for user id: {}", settings.size(), userId);
        return settings;
    }
//4
    @Override
    public NotificationSettingResponseDTO getSettingByWorkspaceAndUser(Long workspaceId, Long userId) {
        log.info("Fetching notification setting for workspace id: {} and user id: {}", workspaceId, userId);
        NotificationSettingResponseDTO setting =  notificationSettingRepository.findAllByWorkspaceIdAndUserId(workspaceId, userId)
                .stream()
                .findFirst()
                .map(notificationSettingMapper::toResponseDTO)
                .orElseThrow(() -> {
            log.warn("Notification setting not found for workspace id: {} and user id: {}", workspaceId, userId);
            return new NotificationSettingNotFoundException("Notification setting tapılmadı: workspaceId=" + workspaceId + ", userId=" + userId);
        });
        log.info("Notification setting found for workspace id: {} and user id: {}", workspaceId, userId);
        return setting;

    }
//5
    @Override
    public NotificationSettingResponseDTO getSettingById(Long id) {
        log.info("Fetching notification setting with id: {}", id);
        NotificationSetting setting = notificationSettingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notification setting not found with id: {}", id);
                    return new NotificationSettingNotFoundException("Notification setting tapılmadı: " + id);
                });
        log.info("Notification setting found with id: {}", id);
        return notificationSettingMapper.toResponseDTO(setting);
    }
//6
    @Override
    public NotificationSettingResponseDTO updateSetting(Long id, NotificationSettingUpdateRequestDTO dto) {
        log.info("Updating notification setting with id: {}", id);
        NotificationSetting setting = notificationSettingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notification setting not found for update. Id: {}", id);
                    return new NotificationSettingNotFoundException("Notification setting tapılmadı: " + id);
                });
        notificationSettingMapper.updateEntity(setting, dto);

        NotificationSetting updated = notificationSettingRepository.save(setting);
        log.info("Notification setting updated successfully. Id: {}", id);
        return notificationSettingMapper.toResponseDTO(updated);
    }
//7
    @Override
    public void deleteSetting(Long id) {
        log.info("Deleting notification setting with id: {}", id);
        NotificationSetting setting = notificationSettingRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Notification setting not found for deletion. Id: {}", id);
                    return new NotificationSettingNotFoundException("Notification setting tapılmadı: " + id);
                });
        notificationSettingRepository.delete(setting);
        log.info("Notification setting deleted successfully. Id: {}", id);
    }
}
