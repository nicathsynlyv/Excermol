package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.WorkspaceCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceUpdateRequestDTO;

import java.util.List;

public interface WorkspaceService {

    // workspace yarat
    WorkspaceResponseDTO createWorkspace(WorkspaceCreateRequestDTO dto);

    // bütün workspace-lər
    List<WorkspaceResponseDTO> getAllWorkspaces();

    // id-yə görə workspace
    WorkspaceResponseDTO getWorkspaceById(Long id);

    // owner-a görə workspace-lər
    List<WorkspaceResponseDTO> getWorkspacesByOwner(Long ownerId);

    // workspace update et - General tab "Update workspace"
    WorkspaceResponseDTO updateWorkspace(Long id, WorkspaceUpdateRequestDTO dto);

    // workspace sil
    void deleteWorkspace(Long id);

    // workspace-i reset et - General tab "Reset workspace"
    void resetWorkspace(Long id);

    // workspace-dən çıx - General tab "Leave workspace"
    void leaveWorkspace(Long workspaceId, Long userId);


}
