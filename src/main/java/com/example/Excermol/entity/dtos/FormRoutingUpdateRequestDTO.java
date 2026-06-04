package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.RoutingCondition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormRoutingUpdateRequestDTO {

    private RoutingCondition conditionType;
    private String conditionValue;
    private String redirectTo;
    private Long emailId;
    private Integer routingOrder;
    private Long formFieldId;
}
