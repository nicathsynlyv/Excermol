package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.PipelineService;
import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.enums.PipelineStatus;
import com.example.Excermol.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PipelineServiceImpl implements PipelineService {

    private final PipelineRepository pipelineRepository;

    @Override
    public List<Pipeline> getAll() {
        return pipelineRepository.findAll();
    }

    @Override
    public Optional<Pipeline> getById(Long id) {
        return pipelineRepository.findById(id);
    }

    @Override
    public Pipeline save(Pipeline entity) {
        return pipelineRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pipelineRepository.deleteById(id);
    }

    @Override
    public List<Pipeline> findByStatus(PipelineStatus status) {
        return pipelineRepository.findByStatus(status);
    }

    @Override
    public List<Pipeline> findByCompanyId(Long companyId) {
        return pipelineRepository.findByCompanyId(companyId);
    }

    @Override
    public List<Pipeline> findByDateBetween(LocalDate start, LocalDate end) {
        return pipelineRepository.findByDateBetween(start, end);
    }
}
