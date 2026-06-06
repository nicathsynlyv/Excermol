package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.WorkspaceService;
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
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workSpaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceMapper workspaceMapper;

    public WorkspaceServiceImpl(WorkspaceRepository workSpaceRepository,
                                UserRepository userRepository,
                                WorkspaceMemberRepository workspaceMemberRepository,
                                WorkspaceMapper workspaceMapper) {
        this.workSpaceRepository = workSpaceRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceMapper = workspaceMapper;
    }


    @Override
    public WorkspaceResponseDTO createWorkspace(WorkspaceCreateRequestDTO dto) {
        // owner tap
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: " + dto.getOwnerId()));

        // workspace yarat
        Workspace workspace = workspaceMapper.toEntity(dto);
        workspace.setOwner(owner);

        Workspace saved = workSpaceRepository.save(workspace);

        // owner-i avtomatik OWNER rolu ilə member əlavə et
        WorkspaceMember ownerMember = new WorkspaceMember();
        ownerMember.setWorkspace(saved);
        ownerMember.setUser(owner);
        ownerMember.setRole(MemberRole.OWNER);
        workspaceMemberRepository.save(ownerMember);

        return workspaceMapper.toResponseDTO(saved);
    }

    @Override
    public List<WorkspaceResponseDTO> getAllWorkspaces() {
        return workSpaceRepository.findAll()
                .stream()
                .map(workspaceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceResponseDTO getWorkspaceById(Long id) {
        Workspace workspace = workSpaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + id));
        return workspaceMapper.toResponseDTO(workspace);
    }

    @Override
    public List<WorkspaceResponseDTO> getWorkspacesByOwner(Long ownerId) {
        return workSpaceRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(workspaceMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public WorkspaceResponseDTO updateWorkspace(Long id, WorkspaceUpdateRequestDTO dto) {
        Workspace workspace = workSpaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + id));

        workspaceMapper.updateEntity(workspace, dto);

        Workspace updated = workSpaceRepository.save(workspace);
        return workspaceMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteWorkspace(Long id) {
        Workspace workspace = workSpaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + id));
        workSpaceRepository.delete(workspace);
    }

    @Override
    public void resetWorkspace(Long id) {
        Workspace workspace = workSpaceRepository.findById(id)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + id));

        // bütün əlaqəli data silinir - CascadeType.ALL avtomatik silir
        workspace.getMembers().clear();
        workspace.getNotificationSettings().clear();
        workspace.getCompanyAttributes().clear();
        workspace.getIntegrations().clear();

        workSpaceRepository.save(workspace);
    }

    @Override
    public void leaveWorkspace(Long workspaceId, Long userId) {
        Workspace workspace = workSpaceRepository.findById(workspaceId)
                .orElseThrow(() -> new WorkspaceNotFoundException("Workspace tapılmadı: " + workspaceId));

        // OWNER workspace-dən çıxa bilməz
        WorkspaceMember member = workspaceMemberRepository
                .findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new MemberNotFoundException("Member tapılmadı"));

        if (member.getRole() == MemberRole.OWNER) {
            throw new OwnerCannotLeaveWorkspaceException("Owner workspace-dən çıxa bilməz");
        }

        workspaceMemberRepository.delete(member);
    }

}
