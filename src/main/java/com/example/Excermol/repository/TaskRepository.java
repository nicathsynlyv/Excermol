package com.example.Excermol.repository;

import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    List<Task> findByStatus(TaskStatus status);

    List<Task> findByTagsContaining(String tag);

    List<Task> findByDueDateBefore(LocalDate date);
}
