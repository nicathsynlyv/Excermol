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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
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

    //1
    @Override
    public CommentResponseDTO createComment(CommentCreateRequestDTO dto) {
        log.info("Creating comment for task id {} ", dto.getTaskId());
        // task tap
        Task task = taskRepository.findById(dto.getTaskId())
                .orElseThrow(() -> {
                    log.warn("Task not found with Id: {}", dto.getTaskId());
                    return new TaskNotFoundException("Task tapılmadı: " + dto.getTaskId());
                });
        // author tap
        User author = userRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> {
                    log.warn("User not found with Id: {}", dto.getAuthorId());
                    return new UserNotFoundException("User tapılmadı: " + dto.getAuthorId());
                });
        Comment comment = commentMapper.toEntity(dto);
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        Comment saved = commentRepository.save(comment);
        log.info("comment created successfully with id: {}", saved.getId());
        return commentMapper.toResponseDTO(saved);
    }

    //2
    @Override
    public List<CommentResponseDTO> getCommentsByTaskId(Long taskId) {
        log.info("Fetching comment for task id: {}", taskId);

        List<CommentResponseDTO> comments = commentRepository.findAllByTaskId(taskId)
                .stream()
                .map(commentMapper::toResponseDTO)
                .collect(Collectors.toList());
        log.info("Retrieved {} comments for task id: {} ", comments.size(), taskId);
        return comments;
    }

    //3
    @Override
    public CommentResponseDTO getCommentById(Long id) {
        log.info("Fetching comment with id: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment not found with id: {}", id);
                    return new CommentNotFoundException("Comment tapılmadı: " + id);
                });
        log.info("Comment found with id: {}", id);
        return commentMapper.toResponseDTO(comment);
    }

    //4
    @Override
    public CommentResponseDTO updateComment(Long id, CommentUpdateRequestDTO dto) {
        log.info("Updateing comment with id: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment not found for update. Id: {}", id);
                    return new CommentNotFoundException("Comment tapılmadı: " + id);
                });
        commentMapper.updateEntity(comment, dto);

        Comment updated = commentRepository.save(comment);
        log.info("Comment Updated successfully. Id: {}", id);
        return commentMapper.toResponseDTO(updated);
    }

    //5
    @Override
    public void deleteComment(Long id) {
        log.info("Deleting comment with id: {}", id);
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Comment not found for deletion. Id: {}", id);
                   return new CommentNotFoundException("Comment tapılmadı: " + id);
                });

        commentRepository.delete(comment);
        log.info("Comment deleted successfully. Id: {}", id);
    }

    //6
    @Override
    public Integer getCommentCountByTaskId(Long taskId) {
        log.info("Fetching comment count for task id: {}", taskId);
        Integer count =  commentRepository.countByTaskId(taskId);
        log.info("Fetched  count for task id: {} is {}", taskId, count);
        return count;
    }
}


