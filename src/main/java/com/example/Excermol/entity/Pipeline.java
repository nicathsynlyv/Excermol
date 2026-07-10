package com.example.Excermol.entity;

import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pipelines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pipeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pipeline adı
    @NotBlank
    @Size(min = 2,max = 100)
    @Column(nullable = false,length = 100)
    private String name;

    // Kart uzerundeki Status: AGENCY,STARTUP,SCALE_UP
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PipelineStatus status;


    // Sutun:WARM,CLOSE,INTERESTED, ENGAGED
    @Enumerated(EnumType.STRING)
    private PipelineStage stage;



    // Əlaqəli şirkət
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Tarix
    private LocalDate date;

    // serf olunan saat
    @Min(0) @Max(9999)
    private Integer hours;

    // Pul dəyəri
    @DecimalMin("0.0")
    @Column(precision = 15, scale = 2)
    private BigDecimal value;

    //    user ile
    @ManyToMany
    @JoinTable(
            name = "pipeline_assignees",
            joinColumns = @JoinColumn(name = "pipeline_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> assignees; // təyin olunmuş komanda

    //     Əlavə məlumat
    @Size(max = 2000)
    @Column(length = 2000)
    private String description;


    // layihe genislendirmek istesem ayri entity yazaram gelecekde

    @Column(length = 500)


    private String callLink;

    @Column(length = 500)
    private String mailLink;

    @Column(length = 500)
    private String chatLink;


}
