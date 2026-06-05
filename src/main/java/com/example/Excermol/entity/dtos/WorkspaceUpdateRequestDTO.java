package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.PhoneCountryCode;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class WorkspaceUpdateRequestDTO {
    private String name;
    private String workspaceAvatar;
    private String currency;
    private PhoneCountryCode phoneCountryCode;
}
