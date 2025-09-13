package com.example.Excermol.controller;

import com.example.Excermol.Service.PipelineService;
import com.example.Excermol.Service.impl.PipelineServiceImpl;
import com.example.Excermol.entity.Pipeline;
import com.example.Excermol.enums.PipelineStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/pipelines")
@RequiredArgsConstructor
public class PipelineController {

    private final PipelineServiceImpl pipelineServiceImpl;

    @Operation(summary = "Bütün pipeline-ları gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla gətirildi")
    })
    @GetMapping
    public ResponseEntity<List<Pipeline>> getAll() {
        return ResponseEntity.ok(pipelineServiceImpl.getAll());
    }

    @Operation(summary = "ID-yə görə pipeline tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline tapıldı"),
            @ApiResponse(responseCode = "404", description = "Pipeline tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Pipeline> getById(@PathVariable Long id) {
        Optional<Pipeline> pipeline = pipelineServiceImpl.getById(id);
        return pipeline.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni pipeline yarat və ya mövcudu yenilə")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline uğurla saxlanıldı")
    })
    @PostMapping
    public ResponseEntity<Pipeline> save(@RequestBody Pipeline pipeline) {
        return ResponseEntity.ok(pipelineServiceImpl.save(pipeline));
    }

    @Operation(summary = "Pipeline sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pipeline uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Pipeline tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pipelineServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Statusa görə pipeline-ları filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla filter olundu")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Pipeline>> findByStatus(@PathVariable PipelineStatus status) {
        return ResponseEntity.ok(pipelineServiceImpl.findByStatus(status));
    }

    @Operation(summary = "Şirkətə görə pipeline-ları filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla filter olundu")
    })
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<Pipeline>> findByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(pipelineServiceImpl.findByCompanyId(companyId));
    }

    @Operation(summary = "Tarix intervalına görə pipeline-ları filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla filter olundu")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<Pipeline>> findByDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return ResponseEntity.ok(pipelineServiceImpl.findByDateBetween(start, end));
    }
}
