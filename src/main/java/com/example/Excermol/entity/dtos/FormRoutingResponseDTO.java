package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.RoutingCondition;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FormRoutingResponseDTO {

    private Long id;
    private RoutingCondition conditionType;
    private String conditionValue;
    private String redirectTo;
    private Integer routingOrder;

    // Form
    private Long formId;

    // FormField
    private Long formFieldId;
    private String formFieldLabel;  // "Work Email Address"

    // Email
    private Long emailId;
    private String emailSubject;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}