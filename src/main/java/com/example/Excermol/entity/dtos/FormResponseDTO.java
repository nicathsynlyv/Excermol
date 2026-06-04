package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.FormStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FormResponseDTO {
    private Long id;
    private String formsName;
    private Integer responsesCount;
    private String links;
    private FormStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // owner bunlar user entityden gelir
    private Long ownerId;
    private String ownerName;
}
