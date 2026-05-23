package com.example.Excermol.repository;

import com.example.Excermol.entity.Task;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // 1. Kanban sütunları üçün — olmasa UI işləməz
    List<Task> findByStatusOrderBySortOrderAsc(TaskStatus status);

    // 2. User-in taskları — "Select user" filter üçün
    List<Task> findByAssignees_Id(Long userId);

    // 3. Axtarış — UI-da search bar var
    List<Task> findByTitleContainingIgnoreCase(String keyword);

    // 4. Priority filter — UI-da filter button var
    List<Task> findByPriority(TaskPriority priority);

    // 5. Tag filter — UI-da Tags button var
    List<Task> findByTags_Id(Long tagId);




}
