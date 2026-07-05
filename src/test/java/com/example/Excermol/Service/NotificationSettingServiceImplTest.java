package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.NotificationSettingServiceImpl;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationSettingServiceImplTest Unit Tests")
public class NotificationSettingServiceImplTest {
    @Mock
    private NotificationSettingRepository notificationSettingRepository;
    @Mock
    private WorkspaceRepository workspaceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private NotificationSettingMapper notificationSettingMapper;

    @InjectMocks
    private NotificationSettingServiceImpl notificationSettingService;

    private NotificationSetting setting;
    private NotificationSettingCreateRequestDTO createDTO;
    private NotificationSettingUpdateRequestDTO updateDTO;
    private NotificationSettingResponseDTO responseDTO;
    private Workspace workspace;
    private User user;

    @BeforeEach
    void setUp() {
        workspace = new Workspace();
        workspace.setId(1L);

        user = new User();
        user.setId(5L);

        setting = new NotificationSetting();
        setting.setId(10L);
        setting.setWorkspace(workspace);
        setting.setUser(user);
        setting.setAnyNewComment(false);
        setting.setAssignedToOrganization(false);
        setting.setFollowingOrganization(false);

        createDTO = new NotificationSettingCreateRequestDTO();
        createDTO.setWorkspaceId(1L);
        createDTO.setUserId(5L);
        createDTO.setAnyNewComment(true);

        updateDTO = new NotificationSettingUpdateRequestDTO();
        updateDTO.setAnyNewComment(true);

        responseDTO = new NotificationSettingResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setWorkspaceId(1L);
        responseDTO.setUserId(5L);
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createNotificationSetting_shouldSaveAndReturnSetting() {
        when(notificationSettingRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(false);
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(notificationSettingMapper.toEntity(createDTO)).thenReturn(setting);
        when(notificationSettingRepository.save(setting)).thenReturn(setting);
        when(notificationSettingMapper.toResponseDTO(setting)).thenReturn(responseDTO);

        NotificationSettingResponseDTO result = notificationSettingService.createNotificationSetting(createDTO);

        assertThat(result).isNotNull();
        assertThat(setting.getWorkspace()).isEqualTo(workspace);
        assertThat(setting.getUser()).isEqualTo(user);
        verify(notificationSettingRepository).save(setting);
    }

    @Test
    void createNotificationSetting_shouldThrowException_whenSettingAlreadyExists() {
        when(notificationSettingRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(true);

        assertThrows(NotificationSettingAlreadyExistsException.class,
                () -> notificationSettingService.createNotificationSetting(createDTO));
        verify(notificationSettingRepository, never()).save(any());
    }

    @Test
    void createNotificationSetting_shouldThrowException_whenWorkspaceNotFound() {
        when(notificationSettingRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(false);
        when(workspaceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class,
                () -> notificationSettingService.createNotificationSetting(createDTO));
        verify(notificationSettingRepository, never()).save(any());
    }

    @Test
    void createNotificationSetting_shouldThrowException_whenUserNotFound() {
        when(notificationSettingRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(false);
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> notificationSettingService.createNotificationSetting(createDTO));
        verify(notificationSettingRepository, never()).save(any());
    }

    // =========================
    // GET BY WORKSPACE ID
    // =========================
    @Test
    void getSettingsByWorkspaceId_shouldReturnSettings() {
        when(notificationSettingRepository.findAllByWorkspaceId(1L)).thenReturn(List.of(setting));
        when(notificationSettingMapper.toResponseDTO(setting)).thenReturn(responseDTO);

        List<NotificationSettingResponseDTO> result = notificationSettingService.getSettingsByWorkspaceId(1L);

        assertThat(result).hasSize(1);
        verify(notificationSettingRepository).findAllByWorkspaceId(1L);
    }

    @Test
    void getSettingsByWorkspaceId_shouldReturnEmptyList_whenNoSettings() {
        when(notificationSettingRepository.findAllByWorkspaceId(1L)).thenReturn(List.of());

        List<NotificationSettingResponseDTO> result = notificationSettingService.getSettingsByWorkspaceId(1L);

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY USER ID
    // =========================
    @Test
    void getSettingsByUserId_shouldReturnSettings() {
        when(notificationSettingRepository.findAllByUserId(5L)).thenReturn(List.of(setting));
        when(notificationSettingMapper.toResponseDTO(setting)).thenReturn(responseDTO);

        List<NotificationSettingResponseDTO> result = notificationSettingService.getSettingsByUserId(5L);

        assertThat(result).hasSize(1);
        verify(notificationSettingRepository).findAllByUserId(5L);
    }

    @Test
    void getSettingsByUserId_shouldReturnEmptyList_whenNoSettings() {
        when(notificationSettingRepository.findAllByUserId(5L)).thenReturn(List.of());

        List<NotificationSettingResponseDTO> result = notificationSettingService.getSettingsByUserId(5L);

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY WORKSPACE AND USER
    // =========================
    @Test
    void getSettingByWorkspaceAndUser_shouldReturnSetting_whenExists() {
        when(notificationSettingRepository.findAllByWorkspaceIdAndUserId(1L, 5L))
                .thenReturn(List.of(setting));
        when(notificationSettingMapper.toResponseDTO(setting)).thenReturn(responseDTO);

        NotificationSettingResponseDTO result =
                notificationSettingService.getSettingByWorkspaceAndUser(1L, 5L);

        assertThat(result.getWorkspaceId()).isEqualTo(1L);
        assertThat(result.getUserId()).isEqualTo(5L);
    }

    @Test
    void getSettingByWorkspaceAndUser_shouldThrowException_whenNotFound() {
        when(notificationSettingRepository.findAllByWorkspaceIdAndUserId(1L, 5L))
                .thenReturn(List.of());

        assertThrows(NotificationSettingNotFoundException.class,
                () -> notificationSettingService.getSettingByWorkspaceAndUser(1L, 5L));
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getSettingById_shouldReturnSetting_whenExists() {
        when(notificationSettingRepository.findById(10L)).thenReturn(Optional.of(setting));
        when(notificationSettingMapper.toResponseDTO(setting)).thenReturn(responseDTO);

        NotificationSettingResponseDTO result = notificationSettingService.getSettingById(10L);

        assertThat(result.getId()).isEqualTo(10L);
        verify(notificationSettingRepository).findById(10L);
    }

    @Test
    void getSettingById_shouldThrowException_whenNotFound() {
        when(notificationSettingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotificationSettingNotFoundException.class,
                () -> notificationSettingService.getSettingById(99L));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateSetting_shouldUpdateAndReturnSetting() {
        when(notificationSettingRepository.findById(10L)).thenReturn(Optional.of(setting));
        when(notificationSettingRepository.save(setting)).thenReturn(setting);
        when(notificationSettingMapper.toResponseDTO(setting)).thenReturn(responseDTO);

        NotificationSettingResponseDTO result = notificationSettingService.updateSetting(10L, updateDTO);

        assertThat(result).isNotNull();
        verify(notificationSettingMapper).updateEntity(setting, updateDTO);
        verify(notificationSettingRepository).save(setting);
    }

    @Test
    void updateSetting_shouldThrowException_whenNotFound() {
        when(notificationSettingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotificationSettingNotFoundException.class,
                () -> notificationSettingService.updateSetting(99L, updateDTO));
        verify(notificationSettingRepository, never()).save(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteSetting_shouldDeleteSetting_whenExists() {
        when(notificationSettingRepository.findById(10L)).thenReturn(Optional.of(setting));

        notificationSettingService.deleteSetting(10L);

        verify(notificationSettingRepository).delete(setting);
    }

    @Test
    void deleteSetting_shouldThrowException_whenNotFound() {
        when(notificationSettingRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotificationSettingNotFoundException.class,
                () -> notificationSettingService.deleteSetting(99L));
        verify(notificationSettingRepository, never()).delete(any());
    }
}
