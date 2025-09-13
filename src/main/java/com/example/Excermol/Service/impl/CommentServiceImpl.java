package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CommentService;
import com.example.Excermol.entity.Comment;
import com.example.Excermol.repository.CommentRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    // BaseService metodları
    @Override
    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    @Override
    public Optional<Comment> getById(Long id) {
        return commentRepository.findById(id);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }

    // CommentService spesifik metod
    @Override
    public List<Comment> findByTask_Id(Long taskId) {
        return commentRepository.findByTask_Id(taskId);
    }


}
