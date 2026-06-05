package com.example.Excermol.mapper;

import com.example.Excermol.entity.Workspace;
import com.example.Excermol.entity.dtos.WorkspaceCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceMapper {

    // CreateRequestDTO → Entity
    public Workspace toEntity(WorkspaceCreateRequestDTO dto) {
        Workspace workspace = new Workspace();
        workspace.setName(dto.getName());
        workspace.setWorkspaceAvatar(dto.getWorkspaceAvatar());
        workspace.setCurrency(dto.getCurrency());
        workspace.setPhoneCountryCode(dto.getPhoneCountryCode());
        // owner Service-də set olunacaq
        return workspace;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(Workspace workspace, WorkspaceUpdateRequestDTO dto) {
        if (dto.getName() != null) {
            workspace.setName(dto.getName());
        }
        if (dto.getWorkspaceAvatar() != null) {
            workspace.setWorkspaceAvatar(dto.getWorkspaceAvatar());
        }
        if (dto.getCurrency() != null) {
            workspace.setCurrency(dto.getCurrency());
        }
        if (dto.getPhoneCountryCode() != null) {
            workspace.setPhoneCountryCode(dto.getPhoneCountryCode());
        }
    }

    // Entity → ResponseDTO
    public WorkspaceResponseDTO toResponseDTO(Workspace workspace) {
        WorkspaceResponseDTO dto = new WorkspaceResponseDTO();
        dto.setId(workspace.getId());
        dto.setName(workspace.getName());
        dto.setWorkspaceAvatar(workspace.getWorkspaceAvatar());
        dto.setCurrency(workspace.getCurrency());
        dto.setPhoneCountryCode(workspace.getPhoneCountryCode());
        dto.setCreatedAt(workspace.getCreatedAt());
        dto.setUpdatedAt(workspace.getUpdatedAt());

        // owner
        if (workspace.getOwner() != null) {
            dto.setOwnerId(workspace.getOwner().getId());
            dto.setOwnerName(workspace.getOwner().getFullName());
        }

        return dto;
    }
}
