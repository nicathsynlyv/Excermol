package com.example.Excermol.entity;

import com.example.Excermol.enums.PipelineStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    // Tarix
    private LocalDate date;

    // Pul dəyəri
    @Column(precision = 15, scale = 2)
    private BigDecimal value;

    // Əlavə məlumat
    @Column(length = 2000)
    private String description;

    // Əlaqəli şirkət
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    // Təyin olunan şəxs
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee_id")
    private User assignee;

    // Menecer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private User manager;

}
