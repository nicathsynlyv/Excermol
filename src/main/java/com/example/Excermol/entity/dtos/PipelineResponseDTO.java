package com.example.Excermol.entity.dtos;

import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class PipelineResponseDTO {
    private Long id;
    private String name;
    private PipelineStatus status;        // AGENCY, STARTUP, SCALE_UP
    private PipelineStage stage;          // ENGAGED, INTERESTED, WARM, CLOSED

    // Company-dən tam object deyil, sadəcə lazım olan məlumatlar
    private Long companyId;
    private String companyName;

    private LocalDate date;
    private Integer hours;
    private BigDecimal value;

    // Assignee-lərdən sadəcə ID və ad
    private List<AssigneeInfo> assignees;

    private String description;
    private String callLink;
    private String mailLink;
    private String chatLink;


    // Nested DTO — tam User qaytarmamaq üçün
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AssigneeInfo {
        private Long id;
        private String fullName;
    }
}
