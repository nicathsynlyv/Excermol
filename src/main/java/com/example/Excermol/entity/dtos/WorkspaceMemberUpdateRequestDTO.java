package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceMemberUpdateRequestDTO {
    @NotNull(message = "Role boş ola bilməz")
    private MemberRole role;  // yalnız rol dəyişir
}
