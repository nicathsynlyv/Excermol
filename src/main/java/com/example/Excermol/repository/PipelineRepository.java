package com.example.Excermol.repository;

import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.enums.PipelineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
    // statusa görə filter
    List<Pipeline> findByStatus(PipelineStatus status);

    // şirkətə görə filter
    List<Pipeline> findByCompanyId(Long companyId);

    // tarix intervalına görə axtarış
    List<Pipeline> findByDateBetween(LocalDate start, LocalDate end);
}
