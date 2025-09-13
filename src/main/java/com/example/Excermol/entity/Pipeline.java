package com.example.Excermol.entity;

import com.example.Excermol.enums.PipelineStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pipelines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pipeline {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pipeline adı
    @Column(nullable = false)
    private String name;

    // Status: ENGAGED, INTERESTED, WARM, CLOSED
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PipelineStatus status;

    // Əlaqəli şirkət
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Tarix
    private LocalDate date;


    // Pul dəyəri
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
    @Column(length = 2000)
    private String description;

    //organization ile
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;


    //pipelineLink ile
    @OneToMany(mappedBy = "pipeline", cascade = CascadeType.ALL)
    private List<PipelineLink> links; // call, mail, chat və s.

}
