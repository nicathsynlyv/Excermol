package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDTO {

    private Long id;
    private String text;

    // task
    private Long taskId;

    // author
    private Long authorId;
    private String authorName;

    private LocalDateTime createdAt;
}
