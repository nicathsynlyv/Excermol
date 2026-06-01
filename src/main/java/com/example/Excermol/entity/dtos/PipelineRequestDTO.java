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
    private String name;

    @NotNull(message = "Status boş ola bilməz")
    private PipelineStatus status;        // AGENCY, STARTUP, SCALE_UP

    @NotNull(message = "Stage boş ola bilməz")
    private PipelineStage stage;          // ENGAGED, INTERESTED, WARM, CLOSED

    @NotNull(message = "Şirkət ID boş ola bilməz")
    private Long companyId;               // Company entity-dən sadəcə ID

    private LocalDate date;

    @Min(value = 0, message = "Saat mənfi ola bilməz")
    private Integer hours;

    @DecimalMin(value = "0.0", message = "Dəyər mənfi ola bilməz")
    private BigDecimal value;

    private List<Long> assigneeIds;       // User entity-dən sadəcə ID-lər

    @Size(max = 2000, message = "Təsvir 2000 simvoldan çox ola bilməz")
    private String description;

    @Size(max = 500)
    private String callLink;

    @Size(max = 500)
    private String mailLink;

    @Size(max = 500)
    private String chatLink;

}
