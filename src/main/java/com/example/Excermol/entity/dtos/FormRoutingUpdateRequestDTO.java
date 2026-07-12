package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.RoutingCondition;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormRoutingUpdateRequestDTO {

    private RoutingCondition conditionType;
    @Size(max = 255, message = "Şərt dəyəri 255 simvoldan çox ola bilməz")
    private String conditionValue;

    @Size(max = 500, message = "Yönləndirmə ünvanı 500 simvoldan çox ola bilməz")
    private String redirectTo;

    private Long emailId;
    private Integer routingOrder;
    private Long formFieldId;
}
