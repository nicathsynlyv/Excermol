package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PersonNoteResponseDTO {
    private Long id;
    private String content;        // "Hello, Designer"
    private String authorEmail;    // "jamankhan@gmail.com"
    private LocalDateTime createdAt; // "About 16min ago"
    private Long personId;
}
