package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.PipelineService;
import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.PipelineRequestDTO;
import com.example.Excermol.entity.dtos.PipelineResponseDTO;
import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import com.example.Excermol.exception.CompanyNotFoundException;
import com.example.Excermol.exception.PipelineNotFoundException;
import com.example.Excermol.mapper.PipelineMapper;
import com.example.Excermol.repository.CompanyRepository;
import com.example.Excermol.repository.PipelineRepository;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
@Slf4j
public class PipelineServiceImpl implements PipelineService {

    private final PipelineRepository pipelineRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final PipelineMapper pipelineMapper;

    public PipelineServiceImpl(PipelineRepository pipelineRepository, CompanyRepository companyRepository, UserRepository userRepository, PipelineMapper pipelineMapper) {
        this.pipelineRepository = pipelineRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.pipelineMapper = pipelineMapper;
    }

    @Override
    public PipelineResponseDTO create(PipelineRequestDTO dto) {
        log.info("Create pipeline for company id: {}",dto.getCompanyId());
        // Company tap
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> {
                    log.warn("Company not found with id: {}", dto.getCompanyId());
                    return new CompanyNotFoundException("Şirkət tapılmadı: " + dto.getCompanyId());
                });

        List<User> assignees = resolveAssignees(dto.getAssigneeIds());


        // DTO → Entity
        Pipeline pipeline = pipelineMapper.toEntity(dto);
        pipeline.setCompany(company);
        pipeline.setAssignees(assignees);

        // Saxla və qaytar
        Pipeline saved = pipelineRepository.save(pipeline);
        log.info("Pipeline created successfully with id: {}", saved.getId());
        return pipelineMapper.toResponseDTO(saved);
    }

    @Override
    public PipelineResponseDTO getById(Long id) {
        log.info("Get pipeline by id: {}", id);
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pipeline not found with id: {}", id);
                    return new PipelineNotFoundException("Pipeline tapılmadı: " + id);
                });
        log.info("pipeline found with id: {}",id);
        return pipelineMapper.toResponseDTO(pipeline);
    }

    @Override
    public PipelineResponseDTO update(Long id, PipelineRequestDTO dto) {
        log.info("Update pipeline with id: {}",id);
        // Mövcud pipeline tap
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pipeline not found for update. Id: {}", id);
                    return new PipelineNotFoundException("Pipeline tapılmadı: " + id);
                });

        // Company tap
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> {
                    log.warn("Company not found with id: {}", dto.getCompanyId());
                    return new CompanyNotFoundException("Şirkət tapılmadı: " + dto.getCompanyId());
                });

        List<User> assignees = resolveAssignees(dto.getAssigneeIds());


        // Mapper ilə yenilə
        pipelineMapper.updateEntity(pipeline, dto);
        pipeline.setCompany(company);
        pipeline.setAssignees(assignees);

        Pipeline updated = pipelineRepository.save(pipeline);
        log.info("Pipeline updated successfully with id: {}", id);
        return pipelineMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {
        log.info("Delete pipeline with id: {}", id);
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Pipeline not found for deletion. Id: {}", id);
                    return new PipelineNotFoundException("Pipeline tapılmadı: " + id);
                });

        pipelineRepository.delete(pipeline);
        log.info("Pipeline deleted successfully. Id: {}", id);

    }

    @Override
    public Page<PipelineResponseDTO> getAll(Pageable pageable) {
        log.info("Fetching all pipelines with pageable: {}", pageable);
        Page<PipelineResponseDTO> pipelines = pipelineRepository.findAll(pageable)
                .map(pipelineMapper::toResponseDTO);

        log.info("Retrieved {} pipelines", pipelines.getTotalElements());
        return pipelines;
    }

    @Override
    public List<PipelineResponseDTO> getByStage(PipelineStage stage) {
        log.info("Fetching pipelines by stage: {}", stage);
        List<PipelineResponseDTO> pipelines = pipelineMapper.toResponseDTOList(
                pipelineRepository.findByStage(stage));

        log.info("Retrieved {} pipelines for stage: {}", pipelines.size(), stage);
        return pipelines;
    }

    @Override
    public List<PipelineResponseDTO> getByStatus(PipelineStatus status) {
        log.info("Fetching pipelines by status: {}", status);
        List<PipelineResponseDTO> pipelines = pipelineMapper.toResponseDTOList(
                pipelineRepository.findByStatus(status));

        log.info("Retrieved {} pipelines for status: {}", pipelines.size(), status);
        return pipelines;
    }

    @Override
    public List<PipelineResponseDTO> getByCompanyId(Long companyId) {
        log.info("Fetching pipelines for company id: {}", companyId);
        List<PipelineResponseDTO> pipelines = pipelineMapper.toResponseDTOList(
                pipelineRepository.findByCompanyId(companyId));

        log.info("Retrieved {} pipelines for company id: {}", pipelines.size(), companyId);
        return pipelines;
    }

    @Override
    public List<PipelineResponseDTO> getByDateBetween(LocalDate start, LocalDate end) {
        log.info("Fetching pipelines between dates: {}-{}", start, end);
        List<PipelineResponseDTO> pipelines = pipelineMapper.toResponseDTOList(
                pipelineRepository.findByDateBetween(start, end));

        log.info("Retrieved {} pipelines between dates: {} - {}", pipelines.size(), start, end);
        return pipelines;
    }

    @Override
    public List<PipelineResponseDTO> getByAssigneeId(Long userId) {
        log.info("Fetching pipelines for assignee id: {}", userId);
        List<PipelineResponseDTO> pipelines = pipelineMapper.toResponseDTOList(
                pipelineRepository.findByAssigneesId(userId));

        log.info("Retrieved {} pipelines for assignee id: {}", pipelines.size(), userId);
        return pipelines;
    }



    // ServiceImpl-in sonuna əlavə edirem cunki dublikate olmasin deye
    private List<User> resolveAssignees(List<Long> assigneeIds) {
        if (assigneeIds == null || assigneeIds.isEmpty()) return List.of();
        log.info("Resolving {} assignees", assigneeIds.size());
        return userRepository.findAllById(assigneeIds);
    }
}