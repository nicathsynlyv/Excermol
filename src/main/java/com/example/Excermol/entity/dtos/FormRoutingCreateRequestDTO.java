package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.RoutingCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormRoutingCreateRequestDTO {

    private RoutingCondition conditionType;  // CONTAINS, EQUALS...
    private String conditionValue;           // "@gmail.com"
    private String redirectTo;               // "Thank you page"
    private Long emailId;                    // Email entity-nin id-si
    private Integer routingOrder;            // sıra
    private Long formId;                     // hansı form-a aiddir
    private Long formFieldId;                // hansı field-i trigger edir
}
