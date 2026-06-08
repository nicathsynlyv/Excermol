package com.example.Excermol.mapper;

import com.example.Excermol.entity.Comment;
import com.example.Excermol.entity.dtos.CommentCreateRequestDTO;
import com.example.Excermol.entity.dtos.CommentResponseDTO;
import com.example.Excermol.entity.dtos.CommentUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    // CreateRequestDTO → Entity
    public Comment toEntity(CommentCreateRequestDTO dto) {
        Comment comment = new Comment();
        comment.setText(dto.getText());
        // task və author Service-də set olunacaq
        return comment;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(Comment comment, CommentUpdateRequestDTO dto) {
        if (dto.getText() != null) {
            comment.setText(dto.getText());
        }
    }

    // Entity → ResponseDTO
    public CommentResponseDTO toResponseDTO(Comment comment) {
        CommentResponseDTO dto = new CommentResponseDTO();
        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());

        // task
        if (comment.getTask() != null) {
            dto.setTaskId(comment.getTask().getId());
        }

        // author
        if (comment.getAuthor() != null) {
            dto.setAuthorId(comment.getAuthor().getId());
            dto.setAuthorName(comment.getAuthor().getFullName());
        }

        return dto;
    }
}