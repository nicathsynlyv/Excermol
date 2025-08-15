package com.example.Excermol.controller;

import com.example.Excermol.Service.PipelineService;
import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.enums.PipelineStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pipelines")
@RequiredArgsConstructor
public class PipelineController {

    private final PipelineService pipelineService;

    // Yeni pipeline yarat
    @PostMapping
    public ResponseEntity<Pipeline> create(@RequestBody Pipeline pipeline) {
        return ResponseEntity.ok(pipelineService.create(pipeline));
    }

    // Bütün pipelineları gətir
    @GetMapping
    public ResponseEntity<List<Pipeline>> getAll() {
        return ResponseEntity.ok(pipelineService.getAll());
    }

    // ID ilə tap
    @GetMapping("/{id}")
    public ResponseEntity<Pipeline> getById(@PathVariable Long id) {
        return ResponseEntity.ok(pipelineService.getById(id));
    }

    // Yenilə
    @PutMapping("/{id}")
    public ResponseEntity<Pipeline> update(@PathVariable Long id, @RequestBody Pipeline pipeline) {
        return ResponseEntity.ok(pipelineService.update(id, pipeline));
    }

    // Sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pipelineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Kanban View üçün qruplaşdırılmış nəticə
    @GetMapping("/kanban")
    public ResponseEntity<Map<PipelineStatus, List<Pipeline>>> getGroupedByStatus() {
        return ResponseEntity.ok(pipelineService.getGroupedByStatus());
    }
}
