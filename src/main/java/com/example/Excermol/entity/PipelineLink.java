package com.example.Excermol.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pipeline_links")
public class PipelineLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;   // email, call, message və s.
    private String url;    // keçid

    @ManyToOne
    @JoinColumn(name = "pipeline_id")
    private Pipeline pipeline;
}
