package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.PhoneCountryCode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkspaceCreateRequestDTO {
    @NotBlank(message = "Workspace adı boş ola bilməz")
    @Size(min = 2,max = 100,message = "Workspace adı 2-100 simvol arasında olmalıdır")
    private String name;

    @Size(max = 500,message = "Avatar URL 500 simvoldan çox ola bilməz")
    private String workspaceAvatar;
    @Size(max = 10,message = "Valyuta kodu 10 simvoldan çox ola bilməz")
    private String currency;


    private PhoneCountryCode phoneCountryCode;
    @NotNull(message = "Owner ID boş ola bilməz")
    private Long ownerId;  // hansı user yaradır
}
