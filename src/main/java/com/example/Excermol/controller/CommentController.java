package com.example.Excermol.controller;

import com.example.Excermol.Service.CommentService;
import com.example.Excermol.entity.dtos.CommentCreateRequestDTO;
import com.example.Excermol.entity.dtos.CommentResponseDTO;
import com.example.Excermol.entity.dtos.CommentUpdateRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment API", description = "Task Comment əməliyyatları")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    //1
    @Operation(summary = "Yeni comment yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment uğurla yaradıldı"),
            @ApiResponse(responseCode = "404", description = "Task və ya User tapılmadı")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(
            @Valid @RequestBody CommentCreateRequestDTO dto) {
        return ResponseEntity.ok(commentService.createComment(dto));
    }

    //2
    @Operation(summary = "Task-a görə bütün commentlər")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByTaskId(
            @PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId));
    }

    //3
    @Operation(summary = "ID ilə comment tap")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment tapıldı"),
            @ApiResponse(responseCode = "404", description = "Comment tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> getCommentById(@PathVariable Long id) {
        return ResponseEntity.ok(commentService.getCommentById(id));
    }

    //4
    @Operation(summary = "Comment update et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Comment tapılmadı")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponseDTO> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody CommentUpdateRequestDTO dto) {
        return ResponseEntity.ok(commentService.updateComment(id, dto));
    }

    //5
    @Operation(summary = "Comment sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Comment tapılmadı")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        commentService.deleteComment(id);
        return ResponseEntity.noContent().build();
    }

    //6
    @Operation(summary = "Task-a görə comment sayı")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping("/task/{taskId}/count")
    public ResponseEntity<Integer> getCommentCountByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentService.getCommentCountByTaskId(taskId));
    }
}