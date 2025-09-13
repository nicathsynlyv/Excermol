package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.PipelineLinkService;
import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.entity.PipelineLink;
import com.example.Excermol.exception.ResourceNotFoundException;
import com.example.Excermol.repository.PipelineLinkRepository;
import com.example.Excermol.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PipelineLinkServiceImpl implements PipelineLinkService {
    private final PipelineLinkRepository pipelineLinkRepository;
    private final PipelineRepository pipelineRepository; // əlavə etməliyik



    @Override
    public List<PipelineLink> getAll() {
        return pipelineLinkRepository.findAll();
    }

    @Override
    public Optional<PipelineLink> getById(Long id) {
        return pipelineLinkRepository.findById(id);
    }

    @Override
    public PipelineLink save(PipelineLink entity) {
        // pipeline yoxlama
        Long pipelineId = entity.getPipeline().getId();
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new ResourceNotFoundException("Pipeline tapılmadı"));

        entity.setPipeline(pipeline);
        return pipelineLinkRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        pipelineLinkRepository.deleteById(id);
    }

    @Override
    public List<PipelineLink> findByPipelineId(Long pipelineId) {
        return pipelineLinkRepository.findByPipelineId(pipelineId);
    }
}
