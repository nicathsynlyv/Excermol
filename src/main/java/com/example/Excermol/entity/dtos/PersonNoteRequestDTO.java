package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonNoteRequestDTO {
    @NotBlank(message = "Məzmun boş ola bilməz")
    @Size(min =1,max = 5000,message = "Məzmun 5000 simvoldan çox ola bilməz")
    private String content;

    @Email(message = "Düzgün email daxil edin")
    @Size(max = 100,message = "Email 100 simvoldan çox ola bilməz")
    private String authorEmail;


    private Long personId;
}
