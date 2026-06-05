package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkspaceMemberResponseDTO {

    private Long id;

    // workspace
    private Long workspaceId;

    // user - Members tabındakı Name, Email kolonları
    private Long userId;
    private String userName;
    private String userEmail;

    // role - OWNER, ADMIN, MEMBER
    private MemberRole role;

    // invite date
    private LocalDateTime inviteDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
