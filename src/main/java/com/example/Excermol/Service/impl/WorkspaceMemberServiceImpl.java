package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.WorkspaceMemberService;
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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkspaceMemberServiceImpl implements WorkspaceMemberService {

    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberMapper workspaceMemberMapper;

    public WorkspaceMemberServiceImpl(WorkspaceMemberRepository workspaceMemberRepository,
                                      WorkspaceRepository workspaceRepository,
                                      UserRepository userRepository,
                                      WorkspaceMemberMapper workspaceMemberMapper) {
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.workspaceMemberMapper = workspaceMemberMapper;
    }

    @Override
    public WorkspaceMemberResponseDTO addMember(WorkspaceMemberCreateRequestDTO dto) {
        // user artıq bu workspace-dədirmi?
        if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(
                dto.getWorkspaceId(), dto.getUserId())) {
            throw new UserAlreadyMemberException("User artıq bu workspace-dədir");
        }

        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + dto.getWorkspaceId()));

        // user tap
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: " + dto.getUserId()));

        // member yarat
        WorkspaceMember member = workspaceMemberMapper.toEntity(dto);
        member.setWorkspace(workspace);
        member.setUser(user);
        member.setInviteDate(LocalDateTime.now());

        WorkspaceMember saved = workspaceMemberRepository.save(member);
        return workspaceMemberMapper.toResponseDTO(saved);
    }

    @Override
    public List<WorkspaceMemberResponseDTO> getMembersByWorkspaceId(Long workspaceId) {
        return workspaceMemberRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(workspaceMemberMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceMemberResponseDTO getMemberById(Long id) {
        WorkspaceMember member = workspaceMemberRepository.findById(id)
                .orElseThrow(() -> new WorkspaceMemberNotFoundException("Member tapılmadı: " + id));
        return workspaceMemberMapper.toResponseDTO(member);
    }

    @Override
    public WorkspaceMemberResponseDTO updateMemberRole(Long id, WorkspaceMemberUpdateRequestDTO dto) {
        WorkspaceMember member = workspaceMemberRepository.findById(id)
                .orElseThrow(() -> new WorkspaceMemberNotFoundException("Member tapılmadı: " + id));

        // OWNER rolunu dəyişmək olmaz
        if (member.getRole() == MemberRole.OWNER) {
            throw new OwnerRoleChangeNotAllowedException("Owner-in rolu dəyişdirilə bilməz");
        }

        workspaceMemberMapper.updateEntity(member, dto);

        WorkspaceMember updated = workspaceMemberRepository.save(member);
        return workspaceMemberMapper.toResponseDTO(updated);
    }

    @Override
    public void removeMember(Long id) {
        WorkspaceMember member = workspaceMemberRepository.findById(id)
                .orElseThrow(() -> new WorkspaceMemberNotFoundException("Member tapılmadı: " + id));

        // OWNER silinə bilməz
        if (member.getRole() == MemberRole.OWNER) {
            throw new OwnerCannotBeRemovedException("Owner workspace-dən silinə bilməz");
        }

        workspaceMemberRepository.delete(member);
    }

    @Override
    public List<WorkspaceMemberResponseDTO> getMembersByRole(Long workspaceId, MemberRole role) {
        return workspaceMemberRepository.findAllByWorkspaceIdAndRole(workspaceId, role)
                .stream()
                .map(workspaceMemberMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
