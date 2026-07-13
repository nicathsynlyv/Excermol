package com.example.Excermol.entity.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentUpdateRequestDTO {

    @NotBlank(message = "Comment mətni boş ola bilməz")
    @Size(min =1, max = 5000,message = "Comment 5000 simvoldan çox ola bilməz")
    private String text; // yalnız text dəyişir
}