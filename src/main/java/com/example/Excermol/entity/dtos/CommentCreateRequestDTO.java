package com.example.Excermol.entity.dtos;

import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequestDTO {

    @NotBlank(message = "Comment mətni boş ola bilməz")
    @Size(min =1,max = 5000,message = "Comment 5000 simvoldan çox ola bilməz")
    private String text;

    @NotNull(message = "Task ID boş ola bilməz")
    private Long taskId;   // hansı task-a aiddir

    @NotNull(message = "Author ID boş ola bilməz")
    private Long authorId; // hansı user yazıb
}