package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.CommentCreateRequestDTO;
import com.example.Excermol.entity.dtos.CommentResponseDTO;
import com.example.Excermol.entity.dtos.CommentUpdateRequestDTO;

import java.util.List;

public interface CommentService {
    //comment yarat
    CommentResponseDTO createComment(CommentCreateRequestDTO dto);

    // task-a görə bütün commentlər
    List<CommentResponseDTO> getCommentsByTaskId(Long taskId);

    // id-yə görə comment
    CommentResponseDTO getCommentById(Long id);

    // comment update et
    CommentResponseDTO updateComment(Long id, CommentUpdateRequestDTO dto);

    // comment sil
    void deleteComment(Long id);

    // task-a görə comment sayı
    Integer getCommentCountByTaskId(Long taskId);
}
