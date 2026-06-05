package com.example.Excermol.mapper;

import com.example.Excermol.entity.WorkspaceMember;
import com.example.Excermol.entity.dtos.WorkspaceMemberCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class WorkspaceMemberMapper {

    // CreateRequestDTO → Entity
    public WorkspaceMember toEntity(WorkspaceMemberCreateRequestDTO dto) {
        WorkspaceMember member = new WorkspaceMember();
        member.setRole(dto.getRole());
        // workspace və user Service-də set olunacaq
        return member;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(WorkspaceMember member, WorkspaceMemberUpdateRequestDTO dto) {
        if (dto.getRole() != null) {
            member.setRole(dto.getRole());
        }
    }

    // Entity → ResponseDTO
    public WorkspaceMemberResponseDTO toResponseDTO(WorkspaceMember member) {
        WorkspaceMemberResponseDTO dto = new WorkspaceMemberResponseDTO();
        dto.setId(member.getId());
        dto.setRole(member.getRole());
        dto.setInviteDate(member.getInviteDate());
        dto.setCreatedAt(member.getCreatedAt());
        dto.setUpdatedAt(member.getUpdatedAt());

        // workspace
        if (member.getWorkspace() != null) {
            dto.setWorkspaceId(member.getWorkspace().getId());
        }

        // user - Name, Email Members tabında görünür
        if (member.getUser() != null) {
            dto.setUserId(member.getUser().getId());
            dto.setUserName(member.getUser().getFullName());
            dto.setUserEmail(member.getUser().getEmail());
        }

        return dto;
    }
}