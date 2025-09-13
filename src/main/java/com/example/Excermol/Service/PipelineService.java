package com.example.Excermol.Service;

import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.enums.PipelineStatus;

import java.time.LocalDate;
import java.util.List;

public interface PipelineService extends BaseService<Pipeline, Long> {
    List<Pipeline> findByStatus(PipelineStatus status);
    List<Pipeline> findByCompanyId(Long companyId);
    List<Pipeline> findByDateBetween(LocalDate start, LocalDate end);
}
