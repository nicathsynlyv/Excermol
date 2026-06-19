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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class WorkspaceServiceImpl implements WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final WorkspaceMapper workspaceMapper;

    public WorkspaceServiceImpl(WorkspaceRepository workspaceRepository,
                                UserRepository userRepository,
                                WorkspaceMemberRepository workspaceMemberRepository,
                                WorkspaceMapper workspaceMapper) {
        this.workspaceRepository = workspaceRepository;
        this.userRepository = userRepository;
        this.workspaceMemberRepository = workspaceMemberRepository;
        this.workspaceMapper = workspaceMapper;
    }

    //1
    @Override
    public WorkspaceResponseDTO createWorkspace(WorkspaceCreateRequestDTO dto) {
        log.info("Creating workspace for owner id: {}", dto.getOwnerId());
        // owner tap
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> {
                    log.warn("User not found with id: {}", dto.getOwnerId());
                    return new UserNotFoundException("User Tapilmadi: " + dto.getOwnerId());
                });

        // workspace yarat
        Workspace workspace = workspaceMapper.toEntity(dto);
        workspace.setOwner(owner);

        Workspace saved = workspaceRepository.save(workspace);
        log.info("Workspace created successfuly with id: {}", saved.getId());

        // owner-i avtomatik OWNER rolu ilə member əlavə et
        WorkspaceMember ownerMember = new WorkspaceMember();
        ownerMember.setWorkspace(saved);
        ownerMember.setUser(owner);
        ownerMember.setRole(MemberRole.OWNER);
        workspaceMemberRepository.save(ownerMember);
        log.info("Workspace added as member with OWNER role for workspace id: {}", saved.getId());

        return workspaceMapper.toResponseDTO(saved);
    }

    //2
    @Override
    public List<WorkspaceResponseDTO> getAllWorkspaces() {
        log.info("Fetching all workspaces");
        List<WorkspaceResponseDTO> workspaces = workspaceRepository.findAll()
                .stream()
                .map(workspaceMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} workspaces", workspaces.size());
        return workspaces;
    }
//3

    @Override
    public WorkspaceResponseDTO getWorkspaceById(Long id) {
        log.info("Fetching workspace with id: {}", id);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Workspace not found with id: {}", id);
                    return new WorkspaceNotFoundException("Workspace tapilmadi: " + id);
                });
        log.info("Workspace found with id: {}", id);
        return workspaceMapper.toResponseDTO(workspace);
    }

    //4
    @Override
    public List<WorkspaceResponseDTO> getWorkspacesByOwner(Long ownerId) {
        log.info("Fetching workspaces for owner id: {}", ownerId);

        List<WorkspaceResponseDTO> workspaces = workspaceRepository.findAllByOwnerId(ownerId)
                .stream()
                .map(workspaceMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} workspaces for owner id: {}", workspaces.size(), ownerId);
        return workspaces;
    }

    //5
    @Override
    public WorkspaceResponseDTO updateWorkspace(Long id, WorkspaceUpdateRequestDTO dto) {
        log.info("Updating workspace with id: {}", id);

        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Workspace not found for update. Id: {}", id);
                    return new WorkspaceNotFoundException("Workspace tapilmadi: " + id);
                });


        workspaceMapper.updateEntity(workspace, dto);

        Workspace updated = workspaceRepository.save(workspace);
        log.info("Workspace updated successfully. Id:{}", id);
        return workspaceMapper.toResponseDTO(updated);
    }

    //6
    @Override
    public void deleteWorkspace(Long id) {
        log.info("Deleting workspace with id: {}", id);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Workspace not found for deletion. Id: {}", id);
                    return new WorkspaceNotFoundException("Workspace tapilmadi: " + id);
                });
        workspaceRepository.delete(workspace);
        log.info("Workspace deleted successfully. Id:{}", id);
    }

    //7
    @Override
    public void resetWorkspace(Long id) {
        log.info("Resetting workspace with id: {}", id);
        Workspace workspace = workspaceRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Worksoace not found for reset. Id: {}", id);
                    return new WorkspaceNotFoundException("Workspace tapilmadi: " + id);
                });
        // bütün əlaqəli data silinir - CascadeType.ALL avtomatik silir
        workspace.getMembers().clear();
        workspace.getNotificationSettings().clear();
        workspace.getCompanyAttributes().clear();
        workspace.getIntegrations().clear();

        workspaceRepository.save(workspace);
        log.info("workspace reset successfully. Id:{}", id);
    }

    //8
    @Override
    public void leaveWorkspace(Long workspaceId, Long userId) {
        log.info("User id: {} attempting to leave workspacei id: {}", userId, workspaceId);
        Workspace workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> {
                    log.warn("Workspace not found for Id: {}", workspaceId);
                    return new WorkspaceNotFoundException("Workspace tapilmadi: " + workspaceId);
                });

        // OWNER workspace-dən çıxa bilməz
        WorkspaceMember member = workspaceMemberRepository
                .findByWorkspaceIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> {
                    log.warn("Member not found for workspace id: {} and User id: {}", workspaceId, userId);
                    return new MemberNotFoundException("Member tapilmadi");
                });

        if (member.getRole() == MemberRole.OWNER) {
            log.warn("Owner attemped to leave workspace id: {}", workspaceId);
            throw new OwnerCannotLeaveWorkspaceException("Owner workspace-dən çıxa bilməz");
        }

        workspaceMemberRepository.delete(member);
        log.info("User id: {} left workspace id: {} successfully", userId, workspaceId);
    }

}
