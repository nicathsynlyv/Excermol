package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.WorkspaceServiceImpl;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.Workspace;
import com.example.Excermol.entity.WorkspaceMember;
import com.example.Excermol.entity.dtos.WorkspaceCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceUpdateRequestDTO;
import com.example.Excermol.enums.MemberRole;
import com.example.Excermol.exception.MemberNotFoundException;
import com.example.Excermol.exception.OwnerCannotLeaveWorkspaceException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.exception.WorkspaceNotFoundException;
import com.example.Excermol.mapper.WorkspaceMapper;
import com.example.Excermol.repository.UserRepository;
import com.example.Excermol.repository.WorkspaceMemberRepository;
import com.example.Excermol.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceImplTest {

    @Mock
    private WorkspaceRepository workspaceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkspaceMemberRepository workspaceMemberRepository;
    @Mock
    private WorkspaceMapper workspaceMapper;

    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    private Workspace workspace;
    private User owner;
    private WorkspaceCreateRequestDTO createDTO;
    private WorkspaceUpdateRequestDTO updateDTO;
    private WorkspaceResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        owner = new User();
        owner.setId(5L);

        workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("Test Workspace");
        workspace.setMembers(new ArrayList<>());
        workspace.setNotificationSettings(new ArrayList<>());
        workspace.setCompanyAttributes(new ArrayList<>());
        workspace.setIntegrations(new ArrayList<>());

        createDTO = new WorkspaceCreateRequestDTO();
        createDTO.setName("Test Workspace");
        createDTO.setOwnerId(5L);

        updateDTO = new WorkspaceUpdateRequestDTO();
        updateDTO.setName("Updated Workspace");

        responseDTO = new WorkspaceResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Workspace");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createWorkspace_shouldSaveWorkspaceAndOwnerMember() {
        when(userRepository.findById(5L)).thenReturn(Optional.of(owner));
        when(workspaceMapper.toEntity(createDTO)).thenReturn(workspace);
        when(workspaceRepository.save(workspace)).thenReturn(workspace);
        when(workspaceMapper.toResponseDTO(workspace)).thenReturn(responseDTO);

        WorkspaceResponseDTO result = workspaceService.createWorkspace(createDTO);

        assertThat(result).isNotNull();
        assertThat(workspace.getOwner()).isEqualTo(owner);
        verify(workspaceRepository).save(workspace);

        ArgumentCaptor<WorkspaceMember> memberCaptor = ArgumentCaptor.forClass(WorkspaceMember.class);
        verify(workspaceMemberRepository).save(memberCaptor.capture());

        WorkspaceMember savedMember = memberCaptor.getValue();
        assertThat(savedMember.getWorkspace()).isEqualTo(workspace);
        assertThat(savedMember.getUser()).isEqualTo(owner);
        assertThat(savedMember.getRole()).isEqualTo(MemberRole.OWNER);
    }

    @Test
    void createWorkspace_shouldThrowException_whenOwnerNotFound() {
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> workspaceService.createWorkspace(createDTO));
        verify(workspaceRepository, never()).save(any());
        verify(workspaceMemberRepository, never()).save(any());
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAllWorkspaces_shouldReturnAllWorkspaces() {
        when(workspaceRepository.findAll()).thenReturn(List.of(workspace));
        when(workspaceMapper.toResponseDTO(workspace)).thenReturn(responseDTO);

        List<WorkspaceResponseDTO> result = workspaceService.getAllWorkspaces();

        assertThat(result).hasSize(1);
        verify(workspaceRepository).findAll();
    }

    @Test
    void getAllWorkspaces_shouldReturnEmptyList_whenNoWorkspaces() {
        when(workspaceRepository.findAll()).thenReturn(List.of());

        List<WorkspaceResponseDTO> result = workspaceService.getAllWorkspaces();

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getWorkspaceById_shouldReturnWorkspace_whenExists() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceMapper.toResponseDTO(workspace)).thenReturn(responseDTO);

        WorkspaceResponseDTO result = workspaceService.getWorkspaceById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(workspaceRepository).findById(1L);
    }

    @Test
    void getWorkspaceById_shouldThrowException_whenNotFound() {
        when(workspaceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.getWorkspaceById(99L));
    }

    // =========================
    // GET BY OWNER
    // =========================
    @Test
    void getWorkspacesByOwner_shouldReturnFilteredWorkspaces() {
        when(workspaceRepository.findAllByOwnerId(5L)).thenReturn(List.of(workspace));
        when(workspaceMapper.toResponseDTO(workspace)).thenReturn(responseDTO);

        List<WorkspaceResponseDTO> result = workspaceService.getWorkspacesByOwner(5L);

        assertThat(result).hasSize(1);
        verify(workspaceRepository).findAllByOwnerId(5L);
    }

    @Test
    void getWorkspacesByOwner_shouldReturnEmptyList_whenNoWorkspaces() {
        when(workspaceRepository.findAllByOwnerId(5L)).thenReturn(List.of());

        List<WorkspaceResponseDTO> result = workspaceService.getWorkspacesByOwner(5L);

        assertThat(result).isEmpty();
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateWorkspace_shouldUpdateAndReturnWorkspace() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceRepository.save(workspace)).thenReturn(workspace);
        when(workspaceMapper.toResponseDTO(workspace)).thenReturn(responseDTO);

        WorkspaceResponseDTO result = workspaceService.updateWorkspace(1L, updateDTO);

        assertThat(result).isNotNull();
        verify(workspaceMapper).updateEntity(workspace, updateDTO);
        verify(workspaceRepository).save(workspace);
    }

    @Test
    void updateWorkspace_shouldThrowException_whenNotFound() {
        when(workspaceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.updateWorkspace(99L, updateDTO));
        verify(workspaceRepository, never()).save(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteWorkspace_shouldDeleteWorkspace_whenExists() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));

        workspaceService.deleteWorkspace(1L);

        verify(workspaceRepository).delete(workspace);
    }

    @Test
    void deleteWorkspace_shouldThrowException_whenNotFound() {
        when(workspaceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.deleteWorkspace(99L));
        verify(workspaceRepository, never()).delete(any());
    }

    // =========================
    // RESET
    // =========================
    @Test
    void resetWorkspace_shouldClearAllRelatedDataAndSave() {
        workspace.getMembers().add(new WorkspaceMember());
        workspace.getNotificationSettings().add(new com.example.Excermol.entity.NotificationSetting());
        workspace.getCompanyAttributes().add(new com.example.Excermol.entity.CompanyAttribute());
        workspace.getIntegrations().add(new com.example.Excermol.entity.Integration());

        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceRepository.save(workspace)).thenReturn(workspace);

        workspaceService.resetWorkspace(1L);

        assertThat(workspace.getMembers()).isEmpty();
        assertThat(workspace.getNotificationSettings()).isEmpty();
        assertThat(workspace.getCompanyAttributes()).isEmpty();
        assertThat(workspace.getIntegrations()).isEmpty();
        verify(workspaceRepository).save(workspace);
    }

    @Test
    void resetWorkspace_shouldThrowException_whenNotFound() {
        when(workspaceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.resetWorkspace(99L));
        verify(workspaceRepository, never()).save(any());
    }

    // =========================
    // LEAVE WORKSPACE
    // =========================
    @Test
    void leaveWorkspace_shouldRemoveMember_whenMemberIsNotOwner() {
        WorkspaceMember member = new WorkspaceMember();
        member.setRole(MemberRole.MEMBER);

        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceMemberRepository.findByWorkspaceIdAndUserId(1L, 10L)).thenReturn(Optional.of(member));

        workspaceService.leaveWorkspace(1L, 10L);

        verify(workspaceMemberRepository).delete(member);
    }

    @Test
    void leaveWorkspace_shouldThrowException_whenWorkspaceNotFound() {
        when(workspaceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceService.leaveWorkspace(99L, 10L));
        verify(workspaceMemberRepository, never()).delete(any());
    }

    @Test
    void leaveWorkspace_shouldThrowException_whenMemberNotFound() {
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceMemberRepository.findByWorkspaceIdAndUserId(1L, 10L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> workspaceService.leaveWorkspace(1L, 10L));
        verify(workspaceMemberRepository, never()).delete(any());
    }

    @Test
    void leaveWorkspace_shouldThrowException_whenMemberIsOwner() {
        WorkspaceMember ownerMember = new WorkspaceMember();
        ownerMember.setRole(MemberRole.OWNER);

        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceMemberRepository.findByWorkspaceIdAndUserId(1L, 5L)).thenReturn(Optional.of(ownerMember));

        assertThrows(OwnerCannotLeaveWorkspaceException.class, () -> workspaceService.leaveWorkspace(1L, 5L));
        verify(workspaceMemberRepository, never()).delete(any());
    }
}