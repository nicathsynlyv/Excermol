package com.example.Excermol.repository;

import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {

    List<Pipeline> findByStage(PipelineStage stage);

    List<Pipeline> findByStatus(PipelineStatus status);

    List<Pipeline> findByCompanyId(Long companyId);

    List<Pipeline> findByDateBetween(LocalDate start, LocalDate end);

    List<Pipeline> findByAssigneesId(Long userId);
}
