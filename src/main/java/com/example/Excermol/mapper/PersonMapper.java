package com.example.Excermol.mapper;

import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.PersonActivity;
import com.example.Excermol.entity.PersonNote;
import com.example.Excermol.entity.Tag;
import com.example.Excermol.entity.dtos.*;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PersonMapper {


    // ===== PERSON =====

    // RequestDTO -> Entity
    public Person toEntity(PersonRequestDTO requestDTO) {
        Person person = new Person();
        person.setFullName(requestDTO.getFullName());
        person.setLastName(requestDTO.getLastName());
        person.setEmail(requestDTO.getEmail());
        person.setJobTitle(requestDTO.getJobTitle());
        person.setWebsiteUrl(requestDTO.getWebsiteUrl());
        person.setPhone(requestDTO.getPhone());
        person.setLinkedinUrl(requestDTO.getLinkedinUrl());
        person.setWhatsappUsername(requestDTO.getWhatsappUsername());
        person.setTwitterName(requestDTO.getTwitterName());
        person.setInstagramName(requestDTO.getInstagramName());
        person.setLeadValue(requestDTO.getLeadValue());
        person.setLists(requestDTO.getLists());
        person.setConnectionStrength(requestDTO.getConnectionStrength());
        person.setStatus(requestDTO.getStatus());
        // companyId, tagIds və userId → service-də set edirik
        return person;
    }

    // Entity -> ResponseDTO
    public PersonResponseDTO toResponseDTO(Person person) {
        PersonResponseDTO responseDTO = new PersonResponseDTO();
        responseDTO.setId(person.getId());
        responseDTO.setFullName(person.getFullName());
        responseDTO.setLastName(person.getLastName());
        responseDTO.setEmail(person.getEmail());
        responseDTO.setJobTitle(person.getJobTitle());
        responseDTO.setWebsiteUrl(person.getWebsiteUrl());
        responseDTO.setPhone(person.getPhone());
        responseDTO.setLinkedinUrl(person.getLinkedinUrl());
        responseDTO.setWhatsappUsername(person.getWhatsappUsername());
        responseDTO.setTwitterName(person.getTwitterName());
        responseDTO.setInstagramName(person.getInstagramName());
        responseDTO.setLeadValue(person.getLeadValue());
        responseDTO.setLists(person.getLists());
        responseDTO.setConnectionStrength(person.getConnectionStrength());
        responseDTO.setStatus(person.getStatus());
        responseDTO.setLastInteractionAt(person.getLastInteractionAt());

        // Company
        if (person.getCompany() != null) {
            responseDTO.setCompanyId(person.getCompany().getId());
            responseDTO.setCompanyName(person.getCompany().getCompanyName());
        }

        // Tags — yalnız adları qaytarırıq
        if (person.getTags() != null) {
            Set<String> tagNames = person.getTags()
                    .stream()
                    .map(Tag::getName)
                    .collect(Collectors.toSet());
            responseDTO.setTagNames(tagNames);
        }

        // User new changes
        if (person.getUser() != null) {
            responseDTO.setUserId(person.getUser().getId());
        }

        return responseDTO;
    }

    // Update metodu
    public void updateEntity(Person person, PersonRequestDTO requestDTO) {
        person.setFullName(requestDTO.getFullName());
        person.setLastName(requestDTO.getLastName());
        person.setEmail(requestDTO.getEmail());
        person.setJobTitle(requestDTO.getJobTitle());
        person.setWebsiteUrl(requestDTO.getWebsiteUrl());
        person.setPhone(requestDTO.getPhone());
        person.setLinkedinUrl(requestDTO.getLinkedinUrl());
        person.setWhatsappUsername(requestDTO.getWhatsappUsername());
        person.setTwitterName(requestDTO.getTwitterName());
        person.setInstagramName(requestDTO.getInstagramName());
        person.setLeadValue(requestDTO.getLeadValue());
        person.setLists(requestDTO.getLists());
        person.setConnectionStrength(requestDTO.getConnectionStrength());
        person.setStatus(requestDTO.getStatus());
    }

    // ===== PERSON NOTE =====

    public PersonNote toNoteEntity(PersonNoteRequestDTO requestDTO) {
        PersonNote note = new PersonNote();
        note.setContent(requestDTO.getContent());
        note.setAuthorEmail(requestDTO.getAuthorEmail());
        // personId → service-də set edirik
        return note;
    }

    public PersonNoteResponseDTO toNoteResponseDTO(PersonNote note) {
        PersonNoteResponseDTO responseDTO = new PersonNoteResponseDTO();
        responseDTO.setId(note.getId());
        responseDTO.setContent(note.getContent());
        responseDTO.setAuthorEmail(note.getAuthorEmail());
        responseDTO.setCreatedAt(note.getCreatedAt());
        responseDTO.setPersonId(note.getPerson().getId());
        return responseDTO;
    }

    // ===== PERSON ACTIVITY =====

    public PersonActivity toActivityEntity(PersonActivityRequestDTO requestDTO) {
        PersonActivity activity = new PersonActivity();
        activity.setAction(requestDTO.getAction());
        activity.setPerformedBy(requestDTO.getPerformedBy());
        // personId → service-də set edirik
        return activity;
    }

    public PersonActivityResponseDTO toActivityResponseDTO(PersonActivity activity) {
        PersonActivityResponseDTO responseDTO = new PersonActivityResponseDTO();
        responseDTO.setId(activity.getId());
        responseDTO.setAction(activity.getAction());
        responseDTO.setPerformedBy(activity.getPerformedBy());
        responseDTO.setPerformedAt(activity.getPerformedAt());

        if (activity.getPerson() != null) {
            responseDTO.setPersonId(activity.getPerson().getId());
            responseDTO.setPersonFullName(
                    activity.getPerson().getFullName() + " " +
                            activity.getPerson().getLastName()
            );
        }

        return responseDTO;
    }
}
