package com.example.Excermol.Service;

import com.example.Excermol.entity.Tag;
import com.example.Excermol.entity.dtos.TagCreateRequestDTO;
import com.example.Excermol.entity.dtos.TagResponseDTO;
import com.example.Excermol.entity.dtos.TagUpdateRequestDTO;

import java.util.List;

public interface TagService {
    // tag yarat
    TagResponseDTO createTag(TagCreateRequestDTO dto);

    // bütün tag-lər
    List<TagResponseDTO> getAllTags();

    // id-yə görə tag
    TagResponseDTO getTagById(Long id);

    // ada görə axtarış
    List<TagResponseDTO> searchTagsByName(String name);

    // tag update et
    TagResponseDTO updateTag(Long id, TagUpdateRequestDTO dto);

    // tag sil
    void deleteTag(Long id);

    // person-a tag əlavə et
    void addTagToPerson(Long tagId, Long personId);

    // person-dan tag sil
    void removeTagFromPerson(Long tagId, Long personId);

    // person-un bütün tag-ləri
    List<TagResponseDTO> getTagsByPersonId(Long personId);
}
