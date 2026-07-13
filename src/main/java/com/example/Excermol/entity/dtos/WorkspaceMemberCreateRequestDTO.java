package com.example.Excermol.entity.dtos;


import com.example.Excermol.enums.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceMemberCreateRequestDTO {

    @NotNull(message = "Workspace Id boş ola bilməz")
    private Long workspaceId;
    @NotNull(message = "User Id boş ola bilməz")
    private Long userId;
    @NotNull(message = "Role boş ola bilməz")
    private MemberRole role;
}