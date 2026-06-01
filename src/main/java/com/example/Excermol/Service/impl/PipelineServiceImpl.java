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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@Transactional
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
        // Company tap
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException("Şirkət tapılmadı: " + dto.getCompanyId()));

        List<User> assignees = resolveAssignees(dto.getAssigneeIds());


        // DTO → Entity
        Pipeline pipeline = pipelineMapper.toEntity(dto);
        pipeline.setCompany(company);
        pipeline.setAssignees(assignees);

        // Saxla və qaytar
        Pipeline saved = pipelineRepository.save(pipeline);
        return pipelineMapper.toResponseDTO(saved);
    }

    @Override
    public PipelineResponseDTO getById(Long id) {
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> new PipelineNotFoundException("Pipeline tapılmadı: " + id));
        return pipelineMapper.toResponseDTO(pipeline);
    }

    @Override
    public PipelineResponseDTO update(Long id, PipelineRequestDTO dto) {
        // Mövcud pipeline tap
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> new PipelineNotFoundException("Pipeline tapılmadı: " + id));

        // Company tap
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new CompanyNotFoundException("Şirkət tapılmadı: " + dto.getCompanyId()));

        List<User> assignees = resolveAssignees(dto.getAssigneeIds());


        // Mapper ilə yenilə
        pipelineMapper.updateEntity(pipeline, dto);
        pipeline.setCompany(company);
        pipeline.setAssignees(assignees);

        Pipeline updated = pipelineRepository.save(pipeline);
        return pipelineMapper.toResponseDTO(updated);
    }

    @Override
    public void delete(Long id) {

        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() ->
                        new PipelineNotFoundException("Pipeline tapılmadı: " + id));

        pipelineRepository.delete(pipeline);
    }

    @Override
    public Page<PipelineResponseDTO> getAll(Pageable pageable) {
        return pipelineRepository.findAll(pageable)
                .map(pipelineMapper::toResponseDTO);
    }

    @Override
    public List<PipelineResponseDTO> getByStage(PipelineStage stage) {
        return pipelineMapper.toResponseDTOList(
                pipelineRepository.findByStage(stage)
        );
    }

    @Override
    public List<PipelineResponseDTO> getByStatus(PipelineStatus status) {
        return pipelineMapper.toResponseDTOList(
                pipelineRepository.findByStatus(status)
        );
    }

    @Override
    public List<PipelineResponseDTO> getByCompanyId(Long companyId) {
        return pipelineMapper.toResponseDTOList(
                pipelineRepository.findByCompanyId(companyId)
        );
    }

    @Override
    public List<PipelineResponseDTO> getByDateBetween(LocalDate start, LocalDate end) {
        return pipelineMapper.toResponseDTOList(
                pipelineRepository.findByDateBetween(start, end)
        );
    }

    @Override
    public List<PipelineResponseDTO> getByAssigneeId(Long userId) {
        return pipelineMapper.toResponseDTOList(
                pipelineRepository.findByAssigneesId(userId)
        );
    }


    // ServiceImpl-in sonuna əlavə edirem cunki dublikate olmasin deye
    private List<User> resolveAssignees(List<Long> assigneeIds) {
        if (assigneeIds == null || assigneeIds.isEmpty()) return List.of();
        return userRepository.findAllById(assigneeIds);
    }
}