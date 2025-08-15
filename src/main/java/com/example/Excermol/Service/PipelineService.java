package com.example.Excermol.Service;

import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.enums.PipelineStatus;
import com.example.Excermol.repository.PipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PipelineService {
    private final PipelineRepository pipelineRepository;

    // Yeni pipeline yarat
    public Pipeline create(Pipeline pipeline) {
        return pipelineRepository.save(pipeline);
    }

    // Hamısını gətir (List View)
    public List<Pipeline> getAll() {
        return pipelineRepository.findAll();
    }

    // ID ilə tap
    public Pipeline getById(Long id) {
        return pipelineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pipeline not found"));
    }

    // Yenilə
    public Pipeline update(Long id, Pipeline newData) {
        Pipeline pipeline = getById(id);
        pipeline.setName(newData.getName());
        pipeline.setStatus(newData.getStatus());
        pipeline.setValue(newData.getValue());
        pipeline.setDate(newData.getDate());
        pipeline.setDescription(newData.getDescription());
        pipeline.setCompany(newData.getCompany());
        pipeline.setAssignee(newData.getAssignee());
        pipeline.setManager(newData.getManager());
        return pipelineRepository.save(pipeline);
    }

    // Sil
    public void delete(Long id) {
        pipelineRepository.deleteById(id);
    }

    // Kanban View üçün qruplaşdırılmış nəticə
    public Map<PipelineStatus, List<Pipeline>> getGroupedByStatus() {
        return pipelineRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(Pipeline::getStatus));
    }
}
