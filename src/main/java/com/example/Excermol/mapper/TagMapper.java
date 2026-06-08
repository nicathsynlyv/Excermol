package com.example.Excermol.mapper;

import com.example.Excermol.entity.Tag;
import com.example.Excermol.entity.dtos.TagCreateRequestDTO;
import com.example.Excermol.entity.dtos.TagResponseDTO;
import com.example.Excermol.entity.dtos.TagUpdateRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class TagMapper {

    // CreateRequestDTO → Entity
    public Tag toEntity(TagCreateRequestDTO dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());
        tag.setColor(dto.getColor());
        return tag;
    }

    // UpdateRequestDTO → Entity (mövcud entity üzərinə)
    public void updateEntity(Tag tag, TagUpdateRequestDTO dto) {
        if (dto.getName() != null) {
            tag.setName(dto.getName());
        }
        if (dto.getColor() != null) {
            tag.setColor(dto.getColor());
        }
    }

    // Entity → ResponseDTO
    public TagResponseDTO toResponseDTO(Tag tag) {
        TagResponseDTO dto = new TagResponseDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        dto.setColor(tag.getColor());

        // neçə person-da istifadə olunur
        if (tag.getPersons() != null) {
            dto.setPersonsCount(tag.getPersons().size());
        } else {
            dto.setPersonsCount(0);
        }

        // neçə task-da istifadə olunur
        if (tag.getTasks() != null) {
            dto.setTasksCount(tag.getTasks().size());
        } else {
            dto.setTasksCount(0);
        }


        return dto;
    }
}
