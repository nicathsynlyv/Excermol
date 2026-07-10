package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PipelineRequestDTO {

    @NotBlank(message = "Pipeline adı boş ola bilməz")
    @Size(min = 2, max = 100,message = "Pipeline adı 2-100 simvol arasında olmalıdır")
    private String name;

    @NotNull(message = "Status boş ola bilməz")
    private PipelineStatus status;        // AGENCY, STARTUP, SCALE_UP

    @NotNull(message = "Stage boş ola bilməz")
    private PipelineStage stage;          // ENGAGED, INTERESTED, WARM, CLOSED

    @NotNull(message = "Şirkət ID boş ola bilməz")
    private Long companyId;               // Company entity-dən sadəcə ID

    private LocalDate date;

    @Min(value = 0, message = "Saat mənfi ola bilməz")
    @Max(value = 9999,message = "Saat 9999 dan çox ola bilməz")
    private Integer hours;

    @DecimalMin(value = "0.0", message = "Dəyər mənfi ola bilməz")
    @DecimalMax(value = "999999999.99", message = "Dəyər həddən artıq böyükdür")
    private BigDecimal value;

    private List<Long> assigneeIds;       // User entity-dən sadəcə ID-lər

    @Size(max = 2000, message = "Təsvir 2000 simvoldan çox ola bilməz")
    private String description;

    @Size(max = 500,message = "Call link 500 simvoldan çox ola bilməz")
    private String callLink;

    @Size(max = 500,message = "Mail link 500 simvoldan çox ola bilməz")
    private String mailLink;

    @Size(max = 500,message = "Chat link 500 simvoldan çox ola bilməz")
    private String chatLink;

}
