package com.example.Excermol.repository;

import com.example.Excermol.entity.PipelineLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PipelineLinkRepository extends JpaRepository<PipelineLink, Long> {
    List<PipelineLink> findByPipelineId(Long pipelineId);
}

