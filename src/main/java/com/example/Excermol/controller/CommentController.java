package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.CommentServiceImpl;
import com.example.Excermol.entity.Comment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentServiceImpl commentServiceImpl;

    public CommentController(CommentServiceImpl commentServiceImpl) {
        this.commentServiceImpl = commentServiceImpl;
    }

    @Operation(summary = "Bütün comment-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi",
                    content = @Content(schema = @Schema(implementation = Comment.class)))
    })
    @GetMapping
    public ResponseEntity<List<Comment>> getAllComments() {
        return ResponseEntity.ok(commentServiceImpl.getAll());
    }

    @Operation(summary = "ID-ə görə comment gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment tapıldı",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Comment tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        return commentServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni comment əlavə edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody Comment comment) {
        return ResponseEntity.status(201).body(commentServiceImpl.save(comment));
    }

    @Operation(summary = "Comment update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment uğurla update olundu",
                    content = @Content(schema = @Schema(implementation = Comment.class))),
            @ApiResponse(responseCode = "404", description = "Comment tapılmadı", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long id, @RequestBody Comment updatedComment) {
        return commentServiceImpl.getById(id)
                .map(comment -> ResponseEntity.ok(commentServiceImpl.save(updatedComment)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Comment silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Comment tapılmadı", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        if (commentServiceImpl.getById(id).isPresent()) {
            commentServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Task-id-ə görə comment-ləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurla gətirildi",
                    content = @Content(schema = @Schema(implementation = Comment.class)))
    })
    @GetMapping("/task/{taskId}")
    public ResponseEntity<List<Comment>> getCommentsByTaskId(@PathVariable Long taskId) {
        return ResponseEntity.ok(commentServiceImpl.findByTask_Id(taskId));
    }
}
