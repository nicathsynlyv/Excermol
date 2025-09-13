package com.example.Excermol.repository;

import com.example.Excermol.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // Task-id-ə görə bütün comment-lər
    List<Comment> findByTask_Id(Long taskId);
}
