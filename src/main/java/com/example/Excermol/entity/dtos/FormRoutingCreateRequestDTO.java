package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.RoutingCondition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormRoutingCreateRequestDTO {


    @NotNull(message = "Şərt tipi boş ola bilməz")
    private RoutingCondition conditionType;  // CONTAINS, EQUALS...

    @NotBlank(message = "Şərt dəyəri boş ola bilməz")
    @Size(max = 250,message = "Şərt dəyəri 255 simvoldan çox ola bilməz")
    private String conditionValue;           // "@gmail.com"

    @NotBlank(message = "Yönləndirmə ünvanı boş ola bilməz")
    @Size(max = 500, message = "Yönləndirmə ünvanı 500 simvoldan çox ola bilməz")
    private String redirectTo;               // "Thank you page"

    private Long emailId;                    // Email entity-nin id-si

    private Integer routingOrder;            // sıra

    @NotNull(message = "Form ID boş ola bilməz")
    private Long formId;                     // hansı form-a aiddir

    @NotNull(message = "FormField ID boş ola bilməz ")
    private Long formFieldId;                // hansı field-i trigger edir
}
