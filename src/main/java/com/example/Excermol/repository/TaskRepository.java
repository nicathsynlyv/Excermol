package com.example.Excermol.repository;

import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    // Status-a görə tapmaq
    List<Task> findByStatus(TaskStatus status);

    // User-id-ə görə (assignee olan task-lar)
    List<Task> findByAssignees_Id(Long userId);

    // Tag-id-ə görə
    List<Task> findByTags_Id(Long tagId);

    List<Task> findByDueDateBetween(LocalDate start, LocalDate end);


}
