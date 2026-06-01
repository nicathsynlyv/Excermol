package com.example.Excermol.controller;

import com.example.Excermol.Service.PipelineService;
import com.example.Excermol.entity.dtos.PipelineRequestDTO;
import com.example.Excermol.entity.dtos.PipelineResponseDTO;
import com.example.Excermol.enums.PipelineStage;
import com.example.Excermol.enums.PipelineStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pipelines")
@Tag(name = "Pipeline", description = "Pipeline əməliyyatları")
public class PipelineController {

    private final PipelineService pipelineService;

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    // CREATE
    @Operation(summary = "Yeni pipeline yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pipeline uğurla yaradıldı"),
            @ApiResponse(responseCode = "400", description = "Məlumatlar düzgün deyil"),
            @ApiResponse(responseCode = "404", description = "Şirkət tapılmadı")
    })
    @PostMapping
    public ResponseEntity<PipelineResponseDTO> create(
            @Valid @RequestBody PipelineRequestDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pipelineService.create(dto));
    }

    // GET BY ID
    @Operation(summary = "ID-yə görə pipeline tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline tapıldı"),
            @ApiResponse(responseCode = "404", description = "Pipeline tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PipelineResponseDTO> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pipelineService.getById(id)
        );
    }

    // GET ALL
    @Operation(
            summary = "Bütün pipeline-ları gətir",
            description = "Pagination dəstəkləyir. Nümunə: ?page=0&size=10&sort=name,asc"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla gətirildi")
    })
    @GetMapping
    public ResponseEntity<Page<PipelineResponseDTO>> getAll(
            Pageable pageable) {

        return ResponseEntity.ok(
                pipelineService.getAll(pageable)
        );
    }

    // UPDATE
    @Operation(summary = "Pipeline yenilə")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline uğurla yeniləndi"),
            @ApiResponse(responseCode = "400", description = "Məlumatlar düzgün deyil"),
            @ApiResponse(responseCode = "404", description = "Pipeline və ya şirkət tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<PipelineResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody PipelineRequestDTO dto) {

        return ResponseEntity.ok(
                pipelineService.update(id, dto)
        );
    }

    // DELETE
    @Operation(summary = "Pipeline sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pipeline uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Pipeline tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id) {

        pipelineService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // FILTER BY STAGE
    @Operation(summary = "Stage-ə görə filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla gətirildi"),
            @ApiResponse(responseCode = "400", description = "Yanlış stage dəyəri")
    })
    @GetMapping("/stage/{stage}")
    public ResponseEntity<List<PipelineResponseDTO>> getByStage(
            @PathVariable PipelineStage stage) {

        return ResponseEntity.ok(
                pipelineService.getByStage(stage)
        );
    }

    // FILTER BY STATUS
    @Operation(summary = "Statusa görə filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla gətirildi"),
            @ApiResponse(responseCode = "400", description = "Yanlış status dəyəri")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PipelineResponseDTO>> getByStatus(
            @PathVariable PipelineStatus status) {

        return ResponseEntity.ok(
                pipelineService.getByStatus(status)
        );
    }

    // FILTER BY COMPANY
    @Operation(summary = "Şirkətə görə filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla gətirildi"),
            @ApiResponse(responseCode = "404", description = "Şirkət tapılmadı")
    })
    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<PipelineResponseDTO>> getByCompany(
            @PathVariable Long companyId) {

        return ResponseEntity.ok(
                pipelineService.getByCompanyId(companyId)
        );
    }

    // FILTER BY DATE RANGE
    @Operation(summary = "Tarix intervalına görə filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla gətirildi"),
            @ApiResponse(responseCode = "400", description = "Tarix formatı yanlışdır")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<PipelineResponseDTO>> getByDateBetween(

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate end) {

        return ResponseEntity.ok(
                pipelineService.getByDateBetween(start, end)
        );
    }

    // FILTER BY ASSIGNEE
    @Operation(summary = "İstifadəçiyə görə filter et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pipeline-lar uğurla gətirildi"),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı")
    })
    @GetMapping("/assignee/{userId}")
    public ResponseEntity<List<PipelineResponseDTO>> getByAssignee(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                pipelineService.getByAssigneeId(userId)
        );
    }
}