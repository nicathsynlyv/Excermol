package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.PersonNoteService;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.PersonNote;
import com.example.Excermol.entity.dtos.PersonNoteRequestDTO;
import com.example.Excermol.entity.dtos.PersonNoteResponseDTO;
import com.example.Excermol.exception.NoteNotFoundException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.mapper.PersonMapper;
import com.example.Excermol.repository.PersonNoteRepository;
import com.example.Excermol.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Slf4j
public class PersonNoteServiceImpl implements PersonNoteService {

    private final PersonNoteRepository personNoteRepository;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonNoteServiceImpl(PersonNoteRepository personNoteRepository,
                                 PersonRepository personRepository,
                                 PersonMapper personMapper) {
        this.personNoteRepository = personNoteRepository;
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public PersonNoteResponseDTO addNote(PersonNoteRequestDTO requestDTO) {
        log.info("Adding note for person id: {}", requestDTO.getPersonId());

        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow(() -> {
                    log.warn("Person not found with id: {}", requestDTO.getPersonId());
                    return new PersonNotFoundException("Person tapılmadı! ID: " + requestDTO.getPersonId());
                });

        PersonNote note = personMapper.toNoteEntity(requestDTO);
        note.setPerson(person);

        PersonNoteResponseDTO response = personMapper.toNoteResponseDTO(personNoteRepository.save(note));
        log.info("Note added successfully with id: {}", response.getId());
        return response;
    }

    @Override
    public List<PersonNoteResponseDTO> getNotesByPersonId(Long personId) {
        log.info("Fetching notes for person id: {}", personId);

        if (!personRepository.existsById(personId)) {
            log.warn("Person not found with id: {}", personId);
            throw new PersonNotFoundException("Person tapılmadı! ID: " + personId);
        }

        List<PersonNoteResponseDTO> notes = personNoteRepository.findByPersonId(personId)
                .stream()
                .map(personMapper::toNoteResponseDTO)
                .toList();

        log.info("Retrieved {} notes for person id: {}", notes.size(), personId);
        return notes;
    }

    @Override
    public PersonNoteResponseDTO updateNote(Long personId, Long noteId, PersonNoteRequestDTO requestDTO) {
        log.info("Updating note with id: {} for person id: {}", noteId, personId);

        PersonNote note = personNoteRepository.findById(noteId)
                .orElseThrow(() -> {
                    log.warn("Note not found with id: {}", noteId);
                    return new NoteNotFoundException("Note tapılmadı! ID: " + noteId);
                });

        if (!note.getPerson().getId().equals(personId)) {
            log.warn("Note id: {} does not belong to person id: {}", noteId, personId);
            throw new PersonNotFoundException("Bu note həmin şəxsə aid deyil!");
        }

        note.setContent(requestDTO.getContent());
        note.setAuthorEmail(requestDTO.getAuthorEmail());

        PersonNoteResponseDTO response = personMapper.toNoteResponseDTO(personNoteRepository.save(note));
        log.info("Note updated successfully. Id: {}", noteId);
        return response;
    }

    @Override
    public void deleteNote(Long personId, Long noteId) {
        log.info("Deleting note with id: {} for person id: {}", noteId, personId);

        PersonNote note = personNoteRepository.findById(noteId)
                .orElseThrow(() -> {
                    log.warn("Note not found with id: {}", noteId);
                    return new NoteNotFoundException("Note tapılmadı! ID: " + noteId);
                });

        if (!note.getPerson().getId().equals(personId)) {
            log.warn("Note id: {} does not belong to person id: {}", noteId, personId);
            throw new PersonNotFoundException("Bu note həmin şəxsə aid deyil!");
        }

        personNoteRepository.delete(note);
        log.info("Note deleted successfully. Id: {}", noteId);
    }
}