package com.example.Excermol.Service;

import com.example.Excermol.entity.Comment;
import com.example.Excermol.repository.CommentRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getCommentsByTask(Long taskId) {
        return commentRepository.findByTask_Id(taskId);
    }

    public Comment addComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        commentRepository.deleteById(id);
    }
}
