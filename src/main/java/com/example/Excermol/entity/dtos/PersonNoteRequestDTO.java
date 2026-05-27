package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonNoteRequestDTO {
    @NotBlank(message = "Məzmun boş ola bilməz")
    private String content;

    private String authorEmail;

    private Long personId;
}
