package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.WorkspaceMemberServiceImpl;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.Workspace;
import com.example.Excermol.entity.WorkspaceMember;
import com.example.Excermol.entity.dtos.WorkspaceMemberCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberUpdateRequestDTO;
import com.example.Excermol.enums.MemberRole;
import com.example.Excermol.exception.*;
import com.example.Excermol.mapper.WorkspaceMemberMapper;
import com.example.Excermol.repository.UserRepository;
import com.example.Excermol.repository.WorkspaceMemberRepository;
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
@DisplayName("WorkspaceMemberImplTest Unit Tests")
public class WorkspaceMemberImplTest {
    @Mock
    private WorkspaceMemberRepository workspaceMemberRepository;
    @Mock
    private WorkspaceRepository workspaceRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private WorkspaceMemberMapper workspaceMemberMapper;

    @InjectMocks
    private WorkspaceMemberServiceImpl workspaceMemberService;

    private WorkspaceMember member;
    private WorkspaceMemberCreateRequestDTO createDTO;
    private WorkspaceMemberUpdateRequestDTO updateDTO;
    private WorkspaceMemberResponseDTO responseDTO;
    private Workspace workspace;
    private User user;

    @BeforeEach
    void setUp() {
        workspace = new Workspace();
        workspace.setId(1L);

        user = new User();
        user.setId(5L);

        member = new WorkspaceMember();
        member.setId(10L);
        member.setRole(MemberRole.MEMBER);
        member.setWorkspace(workspace);
        member.setUser(user);

        createDTO = new WorkspaceMemberCreateRequestDTO();
        createDTO.setWorkspaceId(1L);
        createDTO.setUserId(5L);
        createDTO.setRole(MemberRole.MEMBER);

        updateDTO = new WorkspaceMemberUpdateRequestDTO();
        updateDTO.setRole(MemberRole.ADMIN);

        responseDTO = new WorkspaceMemberResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setRole(MemberRole.MEMBER);
    }

    // =========================
    // ADD MEMBER
    // =========================
    @Test
    void addMember_shouldSaveAndReturnMember() {
        when(workspaceMemberRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(false);
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(workspaceMemberMapper.toEntity(createDTO)).thenReturn(member);
        when(workspaceMemberRepository.save(member)).thenReturn(member);
        when(workspaceMemberMapper.toResponseDTO(member)).thenReturn(responseDTO);

        WorkspaceMemberResponseDTO result = workspaceMemberService.addMember(createDTO);

        assertThat(result).isNotNull();
        assertThat(member.getWorkspace()).isEqualTo(workspace);
        assertThat(member.getUser()).isEqualTo(user);
        assertThat(member.getInviteDate()).isNotNull();
        verify(workspaceMemberRepository).save(member);
    }

    @Test
    void addMember_shouldThrowException_whenUserAlreadyMember() {
        when(workspaceMemberRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(true);

        assertThrows(UserAlreadyMemberException.class, () -> workspaceMemberService.addMember(createDTO));
        verify(workspaceMemberRepository, never()).save(any());
    }

    @Test
    void addMember_shouldThrowException_whenWorkspaceNotFound() {
        when(workspaceMemberRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(false);
        when(workspaceRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(WorkspaceNotFoundException.class, () -> workspaceMemberService.addMember(createDTO));
        verify(workspaceMemberRepository, never()).save(any());
    }

    @Test
    void addMember_shouldThrowException_whenUserNotFound() {
        when(workspaceMemberRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(false);
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> workspaceMemberService.addMember(createDTO));
        verify(workspaceMemberRepository, never()).save(any());
    }

    @Test
    void addMember_shouldSetInviteDate_onAdd() {
        when(workspaceMemberRepository.existsByWorkspaceIdAndUserId(1L, 5L)).thenReturn(false);
        when(workspaceRepository.findById(1L)).thenReturn(Optional.of(workspace));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(workspaceMemberMapper.toEntity(createDTO)).thenReturn(member);
        when(workspaceMemberRepository.save(member)).thenReturn(member);
        when(workspaceMemberMapper.toResponseDTO(member)).thenReturn(responseDTO);

        workspaceMemberService.addMember(createDTO);

        assertThat(member.getInviteDate()).isNotNull();
    }

    // =========================
    // GET BY WORKSPACE ID
    // =========================
    @Test
    void getMembersByWorkspaceId_shouldReturnMembers() {
        when(workspaceMemberRepository.findAllByWorkspaceId(1L)).thenReturn(List.of(member));
        when(workspaceMemberMapper.toResponseDTO(member)).thenReturn(responseDTO);

        List<WorkspaceMemberResponseDTO> result = workspaceMemberService.getMembersByWorkspaceId(1L);

        assertThat(result).hasSize(1);
        verify(workspaceMemberRepository).findAllByWorkspaceId(1L);
    }

    @Test
    void getMembersByWorkspaceId_shouldReturnEmptyList_whenNoMembers() {
        when(workspaceMemberRepository.findAllByWorkspaceId(1L)).thenReturn(List.of());

        List<WorkspaceMemberResponseDTO> result = workspaceMemberService.getMembersByWorkspaceId(1L);

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getMemberById_shouldReturnMember_whenExists() {
        when(workspaceMemberRepository.findById(10L)).thenReturn(Optional.of(member));
        when(workspaceMemberMapper.toResponseDTO(member)).thenReturn(responseDTO);

        WorkspaceMemberResponseDTO result = workspaceMemberService.getMemberById(10L);

        assertThat(result.getId()).isEqualTo(10L);
        verify(workspaceMemberRepository).findById(10L);
    }

    @Test
    void getMemberById_shouldThrowException_whenNotFound() {
        when(workspaceMemberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> workspaceMemberService.getMemberById(99L));
    }

    // =========================
    // UPDATE ROLE
    // =========================
    @Test
    void updateMemberRole_shouldUpdateRoleAndReturnMember() {
        member.setRole(MemberRole.MEMBER);

        when(workspaceMemberRepository.findById(10L)).thenReturn(Optional.of(member));
        when(workspaceMemberRepository.save(member)).thenReturn(member);
        when(workspaceMemberMapper.toResponseDTO(member)).thenReturn(responseDTO);

        WorkspaceMemberResponseDTO result = workspaceMemberService.updateMemberRole(10L, updateDTO);

        assertThat(result).isNotNull();
        verify(workspaceMemberMapper).updateEntity(member, updateDTO);
        verify(workspaceMemberRepository).save(member);
    }

    @Test
    void updateMemberRole_shouldThrowException_whenMemberNotFound() {
        when(workspaceMemberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> workspaceMemberService.updateMemberRole(99L, updateDTO));
        verify(workspaceMemberRepository, never()).save(any());
    }

    @Test
    void updateMemberRole_shouldThrowException_whenMemberIsOwner() {
        member.setRole(MemberRole.OWNER);

        when(workspaceMemberRepository.findById(10L)).thenReturn(Optional.of(member));

        assertThrows(OwnerRoleChangeNotAllowedException.class,
                () -> workspaceMemberService.updateMemberRole(10L, updateDTO));
        verify(workspaceMemberRepository, never()).save(any());
    }

    // =========================
    // REMOVE MEMBER
    // =========================
    @Test
    void removeMember_shouldDeleteMember_whenNotOwner() {
        member.setRole(MemberRole.MEMBER);

        when(workspaceMemberRepository.findById(10L)).thenReturn(Optional.of(member));

        workspaceMemberService.removeMember(10L);

        verify(workspaceMemberRepository).delete(member);
    }

    @Test
    void removeMember_shouldThrowException_whenMemberNotFound() {
        when(workspaceMemberRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> workspaceMemberService.removeMember(99L));
        verify(workspaceMemberRepository, never()).delete(any());
    }

    @Test
    void removeMember_shouldThrowException_whenMemberIsOwner() {
        member.setRole(MemberRole.OWNER);

        when(workspaceMemberRepository.findById(10L)).thenReturn(Optional.of(member));

        assertThrows(OwnerCannotBeRemovedException.class, () -> workspaceMemberService.removeMember(10L));
        verify(workspaceMemberRepository, never()).delete(any());
    }

    // =========================
    // GET BY ROLE
    // =========================
    @Test
    void getMembersByRole_shouldReturnFilteredMembers() {
        when(workspaceMemberRepository.findAllByWorkspaceIdAndRole(1L, MemberRole.MEMBER))
                .thenReturn(List.of(member));
        when(workspaceMemberMapper.toResponseDTO(member)).thenReturn(responseDTO);

        List<WorkspaceMemberResponseDTO> result = workspaceMemberService.getMembersByRole(1L, MemberRole.MEMBER);

        assertThat(result).hasSize(1);
        verify(workspaceMemberRepository).findAllByWorkspaceIdAndRole(1L, MemberRole.MEMBER);
    }

    @Test
    void getMembersByRole_shouldReturnEmptyList_whenNoMatches() {
        when(workspaceMemberRepository.findAllByWorkspaceIdAndRole(1L, MemberRole.ADMIN))
                .thenReturn(List.of());

        List<WorkspaceMemberResponseDTO> result = workspaceMemberService.getMembersByRole(1L, MemberRole.ADMIN);

        assertThat(result).isEmpty();
    }

}
