package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.WorkspaceMemberCreateRequestDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberResponseDTO;
import com.example.Excermol.entity.dtos.WorkspaceMemberUpdateRequestDTO;
import com.example.Excermol.enums.MemberRole;

import java.util.List;

public interface WorkspaceMemberService {

    // üzv əlavə et - "Invite team member"
    WorkspaceMemberResponseDTO addMember(WorkspaceMemberCreateRequestDTO dto);

    // workspace-ə görə bütün üzvlər
    List<WorkspaceMemberResponseDTO> getMembersByWorkspaceId(Long workspaceId);

    // id-yə görə üzv
    WorkspaceMemberResponseDTO getMemberById(Long id);

    // rolu dəyiş - ADMIN, MEMBER, OWNER
    WorkspaceMemberResponseDTO updateMemberRole(Long id, WorkspaceMemberUpdateRequestDTO dto);

    // üzvü sil
    void removeMember(Long id);

    // rola görə üzvlər
    List<WorkspaceMemberResponseDTO> getMembersByRole(Long workspaceId, MemberRole role);
}
