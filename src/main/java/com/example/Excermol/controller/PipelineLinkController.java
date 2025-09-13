package com.example.Excermol.controller;

import com.example.Excermol.Service.PipelineLinkService;
import com.example.Excermol.Service.impl.PipelineLinkServiceImpl;
import com.example.Excermol.entity.PipelineLink;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pipeline-links")
@RequiredArgsConstructor
public class PipelineLinkController {

    private final PipelineLinkServiceImpl pipelineLinkServiceImpl;

    @Operation(summary = "Get all pipeline links")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<PipelineLink>> getAll() {
        List<PipelineLink> links = pipelineLinkServiceImpl.getAll();
        return ResponseEntity.ok(links);
    }

    @Operation(summary = "Get pipeline link by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline link found"),
            @ApiResponse(responseCode = "404", description = "Pipeline link not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PipelineLink> getById(@PathVariable Long id) {
        return pipelineLinkServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create a new pipeline link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pipeline link created"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<PipelineLink> create(@RequestBody PipelineLink pipelineLink) {
        PipelineLink saved = pipelineLinkServiceImpl.save(pipelineLink);
        return ResponseEntity.status(201).body(saved);
    }

    @Operation(summary = "Update an existing pipeline link")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline link updated"),
            @ApiResponse(responseCode = "404", description = "Pipeline link not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PipelineLink> update(@PathVariable Long id, @RequestBody PipelineLink pipelineLink) {
        return pipelineLinkServiceImpl.getById(id)
                .map(existing -> {
                    pipelineLink.setId(id); // ensure the ID is correct
                    PipelineLink updated = pipelineLinkServiceImpl.save(pipelineLink);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete a pipeline link by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pipeline link deleted"),
            @ApiResponse(responseCode = "404", description = "Pipeline link not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return pipelineLinkServiceImpl.getById(id)
                .map(link -> {
                    pipelineLinkServiceImpl.deleteById(id);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get pipeline links by pipeline ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline links retrieved"),
            @ApiResponse(responseCode = "404", description = "No links found for this pipeline")
    })
    @GetMapping("/pipeline/{pipelineId}")
    public ResponseEntity<List<PipelineLink>> getByPipelineId(@PathVariable Long pipelineId) {
        List<PipelineLink> links = pipelineLinkServiceImpl.findByPipelineId(pipelineId);
        if (links.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(links);
    }
}
