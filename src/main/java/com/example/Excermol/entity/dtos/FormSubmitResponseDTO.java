package com.example.Excermol.entity.dtos;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
//FormResponse entiysine uygun
public class FormSubmitResponseDTO {

    private Long id;
    private Long formId;
    private LocalDateTime submittedAt;

    // Connected Contact
    private Long contactId;
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;

    private List<FormAnswerResponseDTO> answers;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}