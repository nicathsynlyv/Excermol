package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.ActivityAction;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PersonActivityResponseDTO {

    private Long id;
    private ActivityAction action;   // CREATED, UPDATED
    private String performedBy;      // "Anwar Hussen"
    private LocalDateTime performedAt; // "About 6d ago"
    private Long personId;
    private String personFullName;   // "Jaman Khan"
}
