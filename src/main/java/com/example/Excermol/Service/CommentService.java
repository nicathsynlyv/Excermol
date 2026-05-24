package com.example.Excermol.Service;

import com.example.Excermol.entity.Comment;

import java.util.List;

public interface CommentService {
    // Task-id-ə görə bütün comment-lər
    List<Comment> findByTask_Id(Long taskId);
}
