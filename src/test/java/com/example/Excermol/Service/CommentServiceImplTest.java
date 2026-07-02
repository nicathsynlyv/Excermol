package com.example.Excermol.Service.impl;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CommentServiceImplTest Unit Tests")
public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    private Task task;
    private User author;
    private Comment comment;
    private CommentCreateRequestDTO createDTO;
    private CommentUpdateRequestDTO updateDTO;
    private CommentResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);

        author = new User();
        author.setId(1L);
        author.setFullName("Nicat Aliyev");

        comment = new Comment();
        comment.setId(1L);
        comment.setText("Test comment");
        comment.setTask(task);
        comment.setAuthor(author);
        comment.setCreatedAt(LocalDateTime.now());

        createDTO = new CommentCreateRequestDTO();
        createDTO.setTaskId(1L);
        createDTO.setAuthorId(1L);
        createDTO.setText("Test comment");

        updateDTO = new CommentUpdateRequestDTO();
        updateDTO.setText("Updated comment");

        responseDTO = new CommentResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setText("Test comment");
        responseDTO.setTaskId(1L);
        responseDTO.setAuthorId(1L);
        responseDTO.setAuthorName("Nicat Aliyev");
    }

    // ---------- createComment ----------

    @Test
    void createComment_success() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(author));
        when(commentMapper.toEntity(createDTO)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);

        CommentResponseDTO result = commentService.createComment(createDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getText(), result.getText());
        verify(commentRepository, times(1)).save(comment);
        verify(taskRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void createComment_taskNotFound_throwsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> commentService.createComment(createDTO));

        verify(userRepository, never()).findById(anyLong());
        verify(commentRepository, never()).save(any());
    }

    @Test
    void createComment_authorNotFound_throwsException() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> commentService.createComment(createDTO));

        verify(commentRepository, never()).save(any());
    }

    // ---------- getCommentsByTaskId ----------

    @Test
    void getCommentsByTaskId_success() {
        Comment comment2 = new Comment();
        comment2.setId(2L);
        comment2.setText("Second comment");

        CommentResponseDTO responseDTO2 = new CommentResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setText("Second comment");

        when(commentRepository.findAllByTaskId(1L)).thenReturn(Arrays.asList(comment, comment2));
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);
        when(commentMapper.toResponseDTO(comment2)).thenReturn(responseDTO2);

        List<CommentResponseDTO> result = commentService.getCommentsByTaskId(1L);

        assertEquals(2, result.size());
        verify(commentRepository, times(1)).findAllByTaskId(1L);
    }

    @Test
    void getCommentsByTaskId_emptyList() {
        when(commentRepository.findAllByTaskId(1L)).thenReturn(List.of());

        List<CommentResponseDTO> result = commentService.getCommentsByTaskId(1L);

        assertTrue(result.isEmpty());
        verify(commentMapper, never()).toResponseDTO(any());
    }

    // ---------- getCommentById ----------

    @Test
    void getCommentById_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);

        CommentResponseDTO result = commentService.getCommentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getCommentById_notFound_throwsException() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.getCommentById(1L));
    }

    // ---------- updateComment ----------

    @Test
    void updateComment_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        when(commentRepository.save(comment)).thenReturn(comment);
        when(commentMapper.toResponseDTO(comment)).thenReturn(responseDTO);

        CommentResponseDTO result = commentService.updateComment(1L, updateDTO);

        assertNotNull(result);
        verify(commentMapper, times(1)).updateEntity(comment, updateDTO);
        verify(commentRepository, times(1)).save(comment);
    }

    @Test
    void updateComment_notFound_throwsException() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.updateComment(1L, updateDTO));

        verify(commentMapper, never()).updateEntity(any(), any());
        verify(commentRepository, never()).save(any());
    }

    // ---------- deleteComment ----------

    @Test
    void deleteComment_success() {
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        commentService.deleteComment(1L);

        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    void deleteComment_notFound_throwsException() {
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CommentNotFoundException.class, () -> commentService.deleteComment(1L));

        verify(commentRepository, never()).delete(any());
    }

    // ---------- getCommentCountByTaskId ----------

    @Test
    void getCommentCountByTaskId_success() {
        when(commentRepository.countByTaskId(1L)).thenReturn(5);

        Integer result = commentService.getCommentCountByTaskId(1L);

        assertEquals(5, result);
        verify(commentRepository, times(1)).countByTaskId(1L);
    }

    @Test
    void getCommentCountByTaskId_zeroComments() {
        when(commentRepository.countByTaskId(1L)).thenReturn(0);

        Integer result = commentService.getCommentCountByTaskId(1L);

        assertEquals(0, result);
    }
}
