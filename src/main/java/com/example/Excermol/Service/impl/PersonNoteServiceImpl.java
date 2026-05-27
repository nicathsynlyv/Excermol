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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
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
        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow(() -> new PersonNotFoundException(
                        "Person tapılmadı! ID: " + requestDTO.getPersonId()));

        PersonNote note = personMapper.toNoteEntity(requestDTO);
        note.setPerson(person);

        return personMapper.toNoteResponseDTO(personNoteRepository.save(note));
    }

    @Override
    public List<PersonNoteResponseDTO> getNotesByPersonId(Long personId) {
        if (!personRepository.existsById(personId)) {
            throw new PersonNotFoundException("Person tapılmadı! ID: " + personId);
        }

        return personNoteRepository.findByPersonId(personId)
                .stream()
                .map(personMapper::toNoteResponseDTO)
                .toList();
    }

    @Override
    public PersonNoteResponseDTO updateNote(Long personId, Long noteId, PersonNoteRequestDTO requestDTO) {
        PersonNote note = personNoteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException(
                        "Note tapılmadı! ID: " + noteId));

        if (!note.getPerson().getId().equals(personId)) {
            throw new PersonNotFoundException("Bu note həmin şəxsə aid deyil!");
        }

        note.setContent(requestDTO.getContent());
        note.setAuthorEmail(requestDTO.getAuthorEmail());

        return personMapper.toNoteResponseDTO(personNoteRepository.save(note));
    }

    @Override
    public void deleteNote(Long personId, Long noteId) {
        PersonNote note = personNoteRepository.findById(noteId)
                .orElseThrow(() -> new NoteNotFoundException(
                        "Note tapılmadı! ID: " + noteId));

        if (!note.getPerson().getId().equals(personId)) {
            throw new PersonNotFoundException("Bu note həmin şəxsə aid deyil!");
        }

        personNoteRepository.delete(note);
    }
}