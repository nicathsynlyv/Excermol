package com.example.Excermol.mapper;


import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.entity.dtos.PipelineRequestDTO;
import com.example.Excermol.entity.dtos.PipelineResponseDTO;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PipelineMapper {

    // RequestDTO → Entity
    public Pipeline toEntity(PipelineRequestDTO dto) {
        Pipeline pipeline = new Pipeline();
        pipeline.setName(dto.getName());
        pipeline.setStatus(dto.getStatus());
        pipeline.setStage(dto.getStage());
        pipeline.setDate(dto.getDate());
        pipeline.setHours(dto.getHours());
        pipeline.setValue(dto.getValue());
        pipeline.setDescription(dto.getDescription());
        pipeline.setCallLink(dto.getCallLink());
        pipeline.setMailLink(dto.getMailLink());
        pipeline.setChatLink(dto.getChatLink());
        // company və assignees → service-də set edilir
        return pipeline;
    }

    // Entity → ResponseDTO
    public PipelineResponseDTO toResponseDTO(Pipeline pipeline) {
        PipelineResponseDTO dto = new PipelineResponseDTO();
        dto.setId(pipeline.getId());
        dto.setName(pipeline.getName());
        dto.setStatus(pipeline.getStatus());
        dto.setStage(pipeline.getStage());
        dto.setDate(pipeline.getDate());
        dto.setHours(pipeline.getHours());
        dto.setValue(pipeline.getValue());
        dto.setDescription(pipeline.getDescription());
        dto.setCallLink(pipeline.getCallLink());
        dto.setMailLink(pipeline.getMailLink());
        dto.setChatLink(pipeline.getChatLink());

        // Company
        if (pipeline.getCompany() != null) {
            dto.setCompanyId(pipeline.getCompany().getId());
            dto.setCompanyName(pipeline.getCompany().getCompanyName());
        }

        // Assignees → AssigneeInfo
        if (pipeline.getAssignees() != null) {
            dto.setAssignees(
                    pipeline.getAssignees().stream()
                            .map(user -> new PipelineResponseDTO.AssigneeInfo(
                                    user.getId(),
                                    user.getFullName()
                            ))
                            .collect(Collectors.toList())
            );
        } else {
            dto.setAssignees(Collections.emptyList());
        }

        return dto;
    }

    // Update — mövcud entity üzərinə yazır
    public void updateEntity(Pipeline pipeline, PipelineRequestDTO dto) {
        pipeline.setName(dto.getName());
        pipeline.setStatus(dto.getStatus());
        pipeline.setStage(dto.getStage());
        pipeline.setDate(dto.getDate());
        pipeline.setHours(dto.getHours());
        pipeline.setValue(dto.getValue());
        pipeline.setDescription(dto.getDescription());
        pipeline.setCallLink(dto.getCallLink());
        pipeline.setMailLink(dto.getMailLink());
        pipeline.setChatLink(dto.getChatLink());
        // company və assignees → service-də update edilir
    }

    // List → ResponseDTO list
    public List<PipelineResponseDTO> toResponseDTOList(List<Pipeline> pipelines) {
        if (pipelines == null) return Collections.emptyList();
        return pipelines.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }
}
