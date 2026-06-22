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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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
        log.info("Adding member - workspaceId: {},userId: {}", dto.getWorkspaceId(), dto.getUserId());
        // user artıq bu workspace-dədirmi?
        if (workspaceMemberRepository.existsByWorkspaceIdAndUserId(dto.getWorkspaceId(), dto.getUserId())) {
            log.warn("User {} already mumber of workspace {}", dto.getUserId(), dto.getWorkspaceId());
            throw new UserAlreadyMemberException("User artıq bu workspace-dədir");
        }

        // workspace tap
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceId())
                .orElseThrow(() -> {
                    log.warn("Workspace not found with Id {}", dto.getWorkspaceId());
                    return new WorkspaceNotFoundException("Workspace tapilmadi" + dto.getWorkspaceId());
                });

        // user tap
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> {
                    log.warn("User not found with Id {}", dto.getUserId());
                    return new UserNotFoundException("User tapilmadi" + dto.getUserId());
                });

        // member yarat
        WorkspaceMember member = workspaceMemberMapper.toEntity(dto);
        member.setWorkspace(workspace);
        member.setUser(user);
        member.setInviteDate(LocalDateTime.now());

        WorkspaceMember saved = workspaceMemberRepository.save(member);
        log.info("Member added successfully. MemberId: {}, WorkspaceId: {},UserId: {}",saved.getId(),dto.getWorkspaceId(),dto.getUserId());
        return workspaceMemberMapper.toResponseDTO(saved);
    }

    @Override
    public List<WorkspaceMemberResponseDTO> getMembersByWorkspaceId(Long workspaceId) {
        log.info("Fetching members by workspaceId: {}", workspaceId);


        List<WorkspaceMemberResponseDTO> members  = workspaceMemberRepository.findAllByWorkspaceId(workspaceId)
                .stream()
                .map(workspaceMemberMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} members for workspaceId: {}", members.size(), workspaceId);
        return members;
    }

    @Override
    public WorkspaceMemberResponseDTO getMemberById(Long id) {
        log.info("Fetching member by id: {}", id);
        WorkspaceMember member = workspaceMemberRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found with Id {}", id);
                    return new MemberNotFoundException("Member Tapilmadi " + id);
                });
        log.info("Member found with id: {}",id);
        return workspaceMemberMapper.toResponseDTO(member);
    }

    @Override
    public WorkspaceMemberResponseDTO updateMemberRole(Long id, WorkspaceMemberUpdateRequestDTO dto) {
        log.info("Update role for member id: {},new role: {}", id, dto.getRole());
        WorkspaceMember member = workspaceMemberRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Member not found for role update. ID {}", id);
                    return new MemberNotFoundException("Member Tapilmadi " + id);
                        });

        // OWNER rolunu dəyişmək olmaz
        if (member.getRole() == MemberRole.OWNER) {
            log.warn("Attempt to change OWNER role.MemberID: {}",id);
            throw new OwnerRoleChangeNotAllowedException("Owner-in rolu dəyişdirilə bilməz");
        }

        workspaceMemberMapper.updateEntity(member, dto);

        WorkspaceMember updated = workspaceMemberRepository.save(member);
        log.info("Member role updated successfully. MemberId: {}, Role: {}",id, dto.getRole());
        return workspaceMemberMapper.toResponseDTO(updated);
    }

    @Override
    public void removeMember(Long id) {
        log.info("Removing member with id: {}", id);
        WorkspaceMember member = workspaceMemberRepository.findById(id)
                .orElseThrow(() ->{
                    log.warn("Member not found for removal. ID {}", id);
                    return new MemberNotFoundException("Member Tapilmadi " + id);
                        });

        // OWNER silinə bilməz
        if (member.getRole() == MemberRole.OWNER) {
            log.warn("Attempt to remove OWNER. MemberID: {}",id);
            throw new OwnerCannotBeRemovedException("Owner workspace-dən silinə bilməz");
        }

        workspaceMemberRepository.delete(member);
        log.info("Member removed successfully. ID: {}",id);
    }

    @Override
    public List<WorkspaceMemberResponseDTO> getMembersByRole(Long workspaceId, MemberRole role) {
        log.info("Fetching Members by workspaceID: {} and role: {}",workspaceId,role);
        List<WorkspaceMemberResponseDTO> members = workspaceMemberRepository.findAllByWorkspaceIdAndRole(workspaceId, role)
                .stream()
                .map(workspaceMemberMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Found {} members with role {} in workspaceiD: {}",members.size(),role,workspaceId);
        return members;
    }
}
