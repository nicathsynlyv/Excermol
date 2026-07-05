package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.IntegrationServiceImpl;
import com.example.Excermol.entity.Integration;
import com.example.Excermol.entity.Workspace;
import com.example.Excermol.entity.dtos.IntegrationCreateRequestDTO;
import com.example.Excermol.entity.dtos.IntegrationResponseDTO;
import com.example.Excermol.entity.dtos.IntegrationUpdateRequestDTO;
import com.example.Excermol.enums.IntegrationType;
import com.example.Excermol.exception.IntegrationAlreadyExistsException;
import com.example.Excermol.exception.IntegrationNotFoundException;
import com.example.Excermol.exception.WorkspaceNotFoundException;
import com.example.Excermol.mapper.IntegrationMapper;
import com.example.Excermol.repository.IntegrationRepository;
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
@DisplayName("IntegrationServiceImplTest Unit Tests")
public class IntegrationServiceImplTest {

    @Mock
    private IntegrationRepository integrationRepository;
    @Mock
    private WorkspaceRepository workspaceRepository;
    @Mock
    private IntegrationMapper integrationMapper;

    @InjectMocks
    private IntegrationServiceImpl integrationService;

    private Integration integration;
    private IntegrationCreateRequestDTO createDTO;
    private IntegrationUpdateRequestDTO updateDTO;
    private IntegrationResponseDTO responseDTO;
    private Workspace workspace;

    @BeforeEach
    void setUp() {
        workspace = new Workspace();
        workspace.setId(1L);

        integration = new Integration();
        integration.setId(10L);
        integration.setIntegrationType(IntegrationType.SLACK);
        integration.setIsActive(false);
        integration.setWorkspace(workspace);

        createDTO = new IntegrationCreateRequestDTO();
        createDTO.setWorkspaceId(1L);
        createDTO.setIntegrationType(IntegrationType.SLACK);
        createDTO.setIsActive(false);

        updateDTO = new IntegrationUpdateRequestDTO();
        updateDTO.setIsActive(true);

        responseDTO = new IntegrationResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setIntegrationType(IntegrationType.SLACK);
        responseDTO.setIsActive(false);
        responseDTO.setWorkspaceId(1L);
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createIntegration_shouldSaveAndReturnIntegration() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(integrationRepository.findByWorkspaceIdAndIntegrationType(1L, IntegrationType.SLACK))
                .thenReturn(Optional.empty());
        when(integrationMapper.toEntity(createDTO)).thenReturn(integration);
        when(integrationRepository.save(integration)).thenReturn(integration);
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        IntegrationResponseDTO result = integrationService.createIntegration(createDTO);

        assertThat(result).isNotNull();
        assertThat(integration.getWorkspace()).isEqualTo(workspace);
        verify(integrationRepository).save(integration);
    }

    @Test
    void createIntegration_shouldThrowException_whenWorkspaceNotFound() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class,
                () -> integrationService.createIntegration(createDTO));
        verify(integrationRepository, never()).save(any());
    }

    @Test
    void createIntegration_shouldThrowException_whenIntegrationAlreadyExists() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(integrationRepository.findByWorkspaceIdAndIntegrationType(1L, IntegrationType.SLACK))
                .thenReturn(Optional.of(integration));

        assertThrows(IntegrationAlreadyExistsException.class,
                () -> integrationService.createIntegration(createDTO));
        verify(integrationRepository, never()).save(any());
    }

    // =========================
    // GET BY WORKSPACE ID
    // =========================
    @Test
    void getIntegrationsByWorkspaceId_shouldReturnIntegrations() {
        when(integrationRepository.findByWorkspaceId(1L)).thenReturn(List.of(integration));
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        List<IntegrationResponseDTO> result = integrationService.getIntegrationsByWorkspaceId(1L);

        assertThat(result).hasSize(1);
        verify(integrationRepository).findByWorkspaceId(1L);
    }

    @Test
    void getIntegrationsByWorkspaceId_shouldReturnEmptyList_whenNoIntegrations() {
        when(integrationRepository.findByWorkspaceId(1L)).thenReturn(List.of());

        List<IntegrationResponseDTO> result = integrationService.getIntegrationsByWorkspaceId(1L);

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getIntegrationById_shouldReturnIntegration_whenExists() {
        when(integrationRepository.findById(10L)).thenReturn(Optional.of(integration));
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        IntegrationResponseDTO result = integrationService.getIntegrationById(10L);

        assertThat(result.getId()).isEqualTo(10L);
        verify(integrationRepository).findById(10L);
    }

    @Test
    void getIntegrationById_shouldThrowException_whenNotFound() {
        when(integrationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class,
                () -> integrationService.getIntegrationById(99L));
    }

    // =========================
    // GET BY TYPE
    // =========================
    @Test
    void getIntegrationByType_shouldReturnIntegration_whenExists() {
        when(integrationRepository.findByWorkspaceIdAndIntegrationType(1L, IntegrationType.SLACK))
                .thenReturn(Optional.of(integration));
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        IntegrationResponseDTO result = integrationService.getIntegrationByType(1L, IntegrationType.SLACK);

        assertThat(result.getIntegrationType()).isEqualTo(IntegrationType.SLACK);
        verify(integrationRepository).findByWorkspaceIdAndIntegrationType(1L, IntegrationType.SLACK);
    }

    @Test
    void getIntegrationByType_shouldThrowException_whenNotFound() {
        when(integrationRepository.findByWorkspaceIdAndIntegrationType(1L, IntegrationType.SLACK))
                .thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class,
                () -> integrationService.getIntegrationByType(1L, IntegrationType.SLACK));
    }

    // =========================
    // GET ACTIVE
    // =========================
    @Test
    void getActiveIntegrations_shouldReturnActiveIntegrations() {
        integration.setIsActive(true);
        when(integrationRepository.findByWorkspaceIdAndIsActive(1L, true)).thenReturn(List.of(integration));
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        List<IntegrationResponseDTO> result = integrationService.getActiveIntegrations(1L);

        assertThat(result).hasSize(1);
        verify(integrationRepository).findByWorkspaceIdAndIsActive(1L, true);
    }

    @Test
    void getActiveIntegrations_shouldReturnEmptyList_whenNoActiveIntegrations() {
        when(integrationRepository.findByWorkspaceIdAndIsActive(1L, true)).thenReturn(List.of());

        List<IntegrationResponseDTO> result = integrationService.getActiveIntegrations(1L);

        assertThat(result).isEmpty();
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateIntegration_shouldUpdateAndReturnIntegration() {
        when(integrationRepository.findById(10L)).thenReturn(Optional.of(integration));
        when(integrationRepository.save(integration)).thenReturn(integration);
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        IntegrationResponseDTO result = integrationService.updateIntegration(10L, updateDTO);

        assertThat(result).isNotNull();
        verify(integrationMapper).updateEntity(integration, updateDTO);
        verify(integrationRepository).save(integration);
    }

    @Test
    void updateIntegration_shouldThrowException_whenNotFound() {
        when(integrationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class,
                () -> integrationService.updateIntegration(99L, updateDTO));
        verify(integrationRepository, never()).save(any());
    }

    // =========================
    // TOGGLE
    // =========================
    @Test
    void toggleIntegration_shouldSetToTrue_whenCurrentlyFalse() {
        integration.setIsActive(false);

        when(integrationRepository.findById(10L)).thenReturn(Optional.of(integration));
        when(integrationRepository.save(integration)).thenReturn(integration);
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        integrationService.toggleIntegration(10L);

        assertThat(integration.getIsActive()).isTrue();
        verify(integrationRepository).save(integration);
    }

    @Test
    void toggleIntegration_shouldSetToFalse_whenCurrentlyTrue() {
        integration.setIsActive(true);

        when(integrationRepository.findById(10L)).thenReturn(Optional.of(integration));
        when(integrationRepository.save(integration)).thenReturn(integration);
        when(integrationMapper.toResponseDTO(integration)).thenReturn(responseDTO);

        integrationService.toggleIntegration(10L);

        assertThat(integration.getIsActive()).isFalse();
        verify(integrationRepository).save(integration);
    }

    @Test
    void toggleIntegration_shouldThrowException_whenNotFound() {
        when(integrationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class,
                () -> integrationService.toggleIntegration(99L));
        verify(integrationRepository, never()).save(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteIntegration_shouldDeleteIntegration_whenExists() {
        when(integrationRepository.findById(10L)).thenReturn(Optional.of(integration));

        integrationService.deleteIntegration(10L);

        verify(integrationRepository).delete(integration);
    }

    @Test
    void deleteIntegration_shouldThrowException_whenNotFound() {
        when(integrationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IntegrationNotFoundException.class,
                () -> integrationService.deleteIntegration(99L));
        verify(integrationRepository, never()).delete(any());
    }
}
