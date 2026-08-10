package com.example.Excermol.mapper;

import com.example.Excermol.entity.Tag;
import com.example.Excermol.entity.Task;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.TaskRequestDto;
import com.example.Excermol.entity.dtos.TaskResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {
    // RequestDto -> Entity
    public Task toEntity(TaskRequestDto dto) {

        Task task = new Task();

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(dto.getDueDate());
        task.setPriority(dto.getPriority());
        task.setStatus(dto.getStatus());
        task.setProgress(dto.getProgress());
        task.setSortOrder(dto.getSortOrder());
        task.setTotalSubtasks(dto.getTotalSubtasks());
        task.setCompletedSubtasks(dto.getCompletedSubtasks());

        return task;
    }

    // Entity -> ResponseDto
    public TaskResponseDto toResponse(Task task) {

        TaskResponseDto dto = new TaskResponseDto();

        // Entity-də database tərəfindən yaradılan ID Response DTO-ya qoyulur. Request-də ID yox idi. Çünki yeni Task yaradarkən ID-ni client müəyyən etmir. Amma response-da client ID-ni bilməlidir.
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setPriority(task.getPriority());
        dto.setStatus(task.getStatus());
        dto.setProgress(task.getProgress());
        dto.setSortOrder(task.getSortOrder());
        dto.setTotalSubtasks(task.getTotalSubtasks());
        dto.setCompletedSubtasks(task.getCompletedSubtasks());

        dto.setCreatedAt(task.getCreatedAt());
        dto.setUpdatedAt(task.getUpdatedAt());

        // COMPANY
        if (task.getCompany() != null) {
            dto.setCompanyName(task.getCompany().getCompanyName());
        }

        // LEAD
        if (task.getLead() != null) {
            dto.setLeadName(task.getLead().getFullName());
        }

        // USERS
        if (task.getAssignees() != null) {
            dto.setAssigneeNames(
                    task.getAssignees()
                            .stream()
                            .map(User::getFullName)
                            .collect(Collectors.toSet())
            );
        }

        // TAGS - mövcud kod
        if (task.getTags() != null) {
            dto.setTagNames(
                    task.getTags()
                            .stream()
                            .map(Tag::getName)
                            .collect(Collectors.toSet())
            );
            // Bunu əlavə et
            dto.setTagIds(
                    task.getTags()
                            .stream()
                            .map(Tag::getId)
                            .collect(Collectors.toSet())
            );
        }

        // COMMENTS COUNT
        if (task.getComments() != null) {
            dto.setCommentsCount(task.getComments().size());
        } else {
            dto.setCommentsCount(0);
        }

        // ATTACHMENTS COUNT
//        if (task.getAttachments() != null) {
//            dto.setAttachmentsCount(task.getAttachments().size());
//        } else {
//            dto.setAttachmentsCount(0);
//        }

        return dto;
    }
    // List<Entity> -> List<ResponseDto>
    public List<TaskResponseDto> toResponseList(List<Task> tasks) {
        if (tasks == null) {
            return java.util.Collections.emptyList();
        }
        return tasks.stream()
                .map(this::toResponse) // Yuxarıdakı təhlükəsiz toResponse metodunu çağırır
                .collect(Collectors.toList());
    }
}


// TaskMapper DTO və Entity arasında mapping etmək üçün istifadə olunur
// toEntity() metodu TaskRequestDto-nu Task entity-sinə çevirir,
// toResponse() isə Task entity-sini TaskResponseDto-ya çevirir.
// Relation-larda isə entity-lərin özünü response-a çıxarmaq əvəzinə yalnız client üçün lazım olan məlumatları, məsələn companyName, leadName, assigneeNames və tagNames-i qaytarıram