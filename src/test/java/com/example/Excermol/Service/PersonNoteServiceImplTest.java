package com.example.Excermol.Service;
import com.example.Excermol.Service.impl.PersonNoteServiceImpl;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.PersonNote;
import com.example.Excermol.entity.dtos.PersonNoteRequestDTO;
import com.example.Excermol.entity.dtos.PersonNoteResponseDTO;
import com.example.Excermol.exception.NoteNotFoundException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.mapper.PersonMapper;
import com.example.Excermol.repository.PersonNoteRepository;
import com.example.Excermol.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonNoteServiceImplTest Unit Tests")
public class PersonNoteServiceImplTest {
    @Mock
    private PersonNoteRepository personNoteRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonNoteServiceImpl personNoteService;

    private Person person;
    private PersonNote note;
    private PersonNoteRequestDTO requestDTO;
    private PersonNoteResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setFullName("John");
        person.setLastName("Doe");

        note = new PersonNote();
        note.setId(10L);
        note.setContent("Test note content");
        note.setAuthorEmail("john@example.com");
        note.setPerson(person);

        requestDTO = new PersonNoteRequestDTO();
        requestDTO.setPersonId(1L);
        requestDTO.setContent("Test note content");
        requestDTO.setAuthorEmail("john@example.com");

        responseDTO = new PersonNoteResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setContent("Test note content");
        responseDTO.setAuthorEmail("john@example.com");
        responseDTO.setPersonId(1L);
    }

    // =========================
    // ADD NOTE
    // =========================
    @Test
    void addNote_shouldSaveAndReturnNote() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toNoteEntity(requestDTO)).thenReturn(note);
        when(personNoteRepository.save(note)).thenReturn(note);
        when(personMapper.toNoteResponseDTO(note)).thenReturn(responseDTO);

        PersonNoteResponseDTO result = personNoteService.addNote(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEqualTo("Test note content");
        assertThat(note.getPerson()).isEqualTo(person);
        verify(personNoteRepository).save(note);
    }

    @Test
    void addNote_shouldThrowException_whenPersonNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class,
                () -> personNoteService.addNote(requestDTO));
        verify(personNoteRepository, never()).save(any());
    }

    @Test
    void addNote_shouldSetPersonOnNote() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toNoteEntity(requestDTO)).thenReturn(note);
        when(personNoteRepository.save(note)).thenReturn(note);
        when(personMapper.toNoteResponseDTO(note)).thenReturn(responseDTO);

        personNoteService.addNote(requestDTO);

        assertThat(note.getPerson()).isEqualTo(person);
    }

    // =========================
    // GET BY PERSON ID
    // =========================
    @Test
    void getNotesByPersonId_shouldReturnNotes_whenPersonExists() {
        when(personRepository.existsById(1L)).thenReturn(true);
        when(personNoteRepository.findByPersonId(1L)).thenReturn(List.of(note));
        when(personMapper.toNoteResponseDTO(note)).thenReturn(responseDTO);

        List<PersonNoteResponseDTO> result = personNoteService.getNotesByPersonId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getContent()).isEqualTo("Test note content");
        verify(personNoteRepository).findByPersonId(1L);
    }

    @Test
    void getNotesByPersonId_shouldReturnEmptyList_whenNoNotes() {
        when(personRepository.existsById(1L)).thenReturn(true);
        when(personNoteRepository.findByPersonId(1L)).thenReturn(List.of());

        List<PersonNoteResponseDTO> result = personNoteService.getNotesByPersonId(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getNotesByPersonId_shouldThrowException_whenPersonNotFound() {
        when(personRepository.existsById(99L)).thenReturn(false);

        assertThrows(PersonNotFoundException.class,
                () -> personNoteService.getNotesByPersonId(99L));
        verify(personNoteRepository, never()).findByPersonId(any());
    }

    // =========================
    // UPDATE NOTE
    // =========================
    @Test
    void updateNote_shouldUpdateAndReturnNote_whenNoteBelogsToPerson() {
        requestDTO.setContent("Updated content");

        when(personNoteRepository.findById(10L)).thenReturn(Optional.of(note));
        when(personNoteRepository.save(note)).thenReturn(note);
        when(personMapper.toNoteResponseDTO(note)).thenReturn(responseDTO);

        PersonNoteResponseDTO result = personNoteService.updateNote(1L, 10L, requestDTO);

        assertThat(result).isNotNull();
        assertThat(note.getContent()).isEqualTo("Updated content");
        assertThat(note.getAuthorEmail()).isEqualTo("john@example.com");
        verify(personNoteRepository).save(note);
    }

    @Test
    void updateNote_shouldThrowException_whenNoteNotFound() {
        when(personNoteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class,
                () -> personNoteService.updateNote(1L, 99L, requestDTO));
        verify(personNoteRepository, never()).save(any());
    }

    @Test
    void updateNote_shouldThrowException_whenNoteDoesNotBelongToPerson() {
        Person otherPerson = new Person();
        otherPerson.setId(2L);
        note.setPerson(otherPerson);

        when(personNoteRepository.findById(10L)).thenReturn(Optional.of(note));

        // note person id=2, amma personId=1 verilir
        assertThrows(PersonNotFoundException.class,
                () -> personNoteService.updateNote(1L, 10L, requestDTO));
        verify(personNoteRepository, never()).save(any());
    }

    @Test
    void updateNote_shouldUpdateContentAndAuthorEmail() {
        requestDTO.setContent("New Content");
        requestDTO.setAuthorEmail("new@example.com");

        when(personNoteRepository.findById(10L)).thenReturn(Optional.of(note));
        when(personNoteRepository.save(note)).thenReturn(note);
        when(personMapper.toNoteResponseDTO(note)).thenReturn(responseDTO);

        personNoteService.updateNote(1L, 10L, requestDTO);

        assertThat(note.getContent()).isEqualTo("New Content");
        assertThat(note.getAuthorEmail()).isEqualTo("new@example.com");
    }

    // =========================
    // DELETE NOTE
    // =========================
    @Test
    void deleteNote_shouldDeleteNote_whenNoteBelongsToPerson() {
        when(personNoteRepository.findById(10L)).thenReturn(Optional.of(note));

        personNoteService.deleteNote(1L, 10L);

        verify(personNoteRepository).delete(note);
    }

    @Test
    void deleteNote_shouldThrowException_whenNoteNotFound() {
        when(personNoteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoteNotFoundException.class,
                () -> personNoteService.deleteNote(1L, 99L));
        verify(personNoteRepository, never()).delete(any());
    }

    @Test
    void deleteNote_shouldThrowException_whenNoteDoesNotBelongToPerson() {
        Person otherPerson = new Person();
        otherPerson.setId(2L);
        note.setPerson(otherPerson);

        when(personNoteRepository.findById(10L)).thenReturn(Optional.of(note));

        // note person id=2, amma personId=1 verilir
        assertThrows(PersonNotFoundException.class,
                () -> personNoteService.deleteNote(1L, 10L));
        verify(personNoteRepository, never()).delete(any());
    }
}
