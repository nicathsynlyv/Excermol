package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentCreateRequestDTO {

    private String text;
    private Long taskId;   // hansı task-a aiddir
    private Long authorId; // hansı user yazıb
}