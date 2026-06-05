package com.example.Excermol.entity.dtos;


import com.example.Excermol.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceMemberCreateRequestDTO {

    private Long workspaceId;
    private Long userId;
    private MemberRole role;
}