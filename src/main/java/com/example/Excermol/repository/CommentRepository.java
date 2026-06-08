package com.example.Excermol.repository;

import com.example.Excermol.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // task-a görə bütün commentlər
    List<Comment> findAllByTaskId(Long taskId);

    // user-ə görə bütün commentlər
    List<Comment> findAllByAuthorId(Long authorId);

    // task-a görə comment sayı
    Integer countByTaskId(Long taskId);
}
