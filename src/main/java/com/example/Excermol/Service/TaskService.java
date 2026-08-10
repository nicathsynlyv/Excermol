package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import com.example.Excermol.enums.TaskPriority;
import com.example.Excermol.enums.TaskStatus;

import java.util.List;
//TaskService burada müqavilə (contract) rolunu oynayır.
public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto dto);

    TaskResponseDto updateTask(Long id, TaskRequestDto dto);

    TaskResponseDto getById(Long id);

    List<TaskResponseDto> getAll();

    List<TaskResponseDto> findByStatus(TaskStatus status);

    List<TaskResponseDto> findByUser(Long userId);

    List<TaskResponseDto> search(String keyword);

    List<TaskResponseDto> findByPriority(TaskPriority priority);

    List<TaskResponseDto> findByTag(Long tagId);

    void delete(Long id);


}

// Service interface-i abstraction və loose coupling üçün yazmışam. Controller konkret TaskServiceImpl-dən yox, TaskService interface-dən asılıdır. Beləliklə implementation ilə istifadə edən tərəf bir-birindən ayrılır
// test yazmaq və gələcəkdə başqa implementation əlavə etmək daha rahat olur




//TaskService Task modulunun biznes əməliyyatlarını müəyyən edən service interface-dir
// Burada create, update, get, search, filter və delete
// kimi əməliyyatların müqaviləsi müəyyən olunur.
// Konkret biznes məntiqi isə TaskServiceImpl-də implement edilir.
