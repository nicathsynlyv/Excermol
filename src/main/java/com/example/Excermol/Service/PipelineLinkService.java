package com.example.Excermol.Service;

import com.example.Excermol.entity.PipelineLink;

import java.util.List;

public interface PipelineLinkService extends BaseService<PipelineLink, Long> {
    List<PipelineLink> findByPipelineId(Long pipelineId);
}
