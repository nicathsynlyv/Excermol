package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CommentService;
import com.example.Excermol.entity.Comment;
import com.example.Excermol.entity.Task;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.CommentCreateRequestDTO;
import com.example.Excermol.entity.dtos.CommentResponseDTO;
import com.example.Excermol.entity.dtos.CommentUpdateRequestDTO;
import com.example.Excermol.exception.CommentNotFoundException;
import com.example.Excermol.exception.TaskNotFoundException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.CommentMapper;
import com.example.Excermol.repository.CommentRepository;
import com.example.Excermol.repository.TaskRepository;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              TaskRepository taskRepository,
                              UserRepository userRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    @Override
    public CommentResponseDTO createComment(CommentCreateRequestDTO dto) {
        // task tap
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> new TaskNotFoundException("Task tapılmadı: " + dto.getTaskId()));

        // author tap
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new UserNotFoundException("User tapılmadı: " + dto.getAuthorId()));

        Comment comment = commentMapper.toEntity(dto);
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        return commentMapper.toResponseDTO(saved);
    }

    @Override
    public List<CommentResponseDTO> getCommentsByTaskId(Long taskId) {
        return commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(commentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponseDTO getCommentById(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment tapılmadı: " + id));
        return commentMapper.toResponseDTO(comment);
    }

    @Override
    public CommentResponseDTO updateComment(Long id, CommentUpdateRequestDTO dto) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment tapılmadı: " + id));

        commentMapper.updateEntity(comment, dto);

        Comment updated = commentRepository.save(comment);
        return commentMapper.toResponseDTO(updated);
    }

    @Override
    public void deleteComment(Long id) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment tapılmadı: " + id));
        commentRepository.delete(comment);
    }

    @Override
    public Integer getCommentCountByTaskId(Long taskId) {
        return commentRepository.countByTaskId(taskId);
    }
}


