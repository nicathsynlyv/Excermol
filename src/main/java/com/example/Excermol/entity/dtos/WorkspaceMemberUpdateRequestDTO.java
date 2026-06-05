package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceMemberUpdateRequestDTO {

    private MemberRole role;  // yalnız rol dəyişir
}
