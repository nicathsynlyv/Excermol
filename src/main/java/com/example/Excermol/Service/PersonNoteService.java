package com.example.Excermol.Service;

import com.example.Excermol.entity.dtos.PersonNoteRequestDTO;
import com.example.Excermol.entity.dtos.PersonNoteResponseDTO;

import java.util.List;

public interface PersonNoteService {
    // Note əlavə et — UI-da "Type anything..." input
    PersonNoteResponseDTO addNote(PersonNoteRequestDTO requestDTO);

    // Şəxsə aid bütün notları gətir — UI-da Notes tab
    List<PersonNoteResponseDTO> getNotesByPersonId(Long personId);
    void deleteNote(Long personId, Long noteId);

    PersonNoteResponseDTO updateNote(Long personId, Long noteId, PersonNoteRequestDTO requestDTO);
}
