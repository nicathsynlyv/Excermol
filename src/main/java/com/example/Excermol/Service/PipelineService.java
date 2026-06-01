package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.PipelineRequestDTO;
import com.example.Excermol.entity.dtos.PipelineResponseDTO;
import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PipelineService {

    // CRUD
    PipelineResponseDTO create(PipelineRequestDTO dto);

    PipelineResponseDTO getById(Long id);

    PipelineResponseDTO update(Long id, PipelineRequestDTO dto);

    void delete(Long id);

    // Pagination
    Page<PipelineResponseDTO> getAll(Pageable pageable);

    // Filters
    List<PipelineResponseDTO> getByStage(PipelineStage stage);

    List<PipelineResponseDTO> getByStatus(PipelineStatus status);

    List<PipelineResponseDTO> getByCompanyId(Long companyId);

    List<PipelineResponseDTO> getByDateBetween(LocalDate start, LocalDate end);

    List<PipelineResponseDTO> getByAssigneeId(Long userId);
}