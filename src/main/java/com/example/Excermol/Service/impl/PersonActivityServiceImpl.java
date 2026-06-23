package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.PersonActivityService;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.PersonActivity;
import com.example.Excermol.entity.dtos.PersonActivityRequestDTO;
import com.example.Excermol.entity.dtos.PersonActivityResponseDTO;
import com.example.Excermol.exception.ActivityNotFoundException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.mapper.PersonMapper;
import com.example.Excermol.repository.PersonActivityRepository;
import com.example.Excermol.repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
@Slf4j
public class PersonActivityServiceImpl implements PersonActivityService {

    private final PersonActivityRepository personActivityRepository;
    private final PersonRepository personRepository;
    private final PersonMapper personMapper;

    public PersonActivityServiceImpl(PersonActivityRepository personActivityRepository,
                                     PersonRepository personRepository,
                                     PersonMapper personMapper) {
        this.personActivityRepository = personActivityRepository;
        this.personRepository = personRepository;
        this.personMapper = personMapper;
    }

    @Override
    public PersonActivityResponseDTO addActivity(PersonActivityRequestDTO requestDTO) {
        log.info("Adding activity for person id: {}", requestDTO.getPersonId());

        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow(() -> {
                    log.warn("Person not found with id: {}", requestDTO.getPersonId());
                    return new PersonNotFoundException("Person tapılmadı! ID: " + requestDTO.getPersonId());
                });

        PersonActivity activity = personMapper.toActivityEntity(requestDTO);
        activity.setPerson(person);

        PersonActivityResponseDTO response = personMapper.toActivityResponseDTO(personActivityRepository.save(activity));
        log.info("Activity added successfully with id: {}", response.getId());
        return response;
    }

    @Override
    public List<PersonActivityResponseDTO> getActivitiesByPersonId(Long personId) {
        log.info("Fetching activities for person id: {}", personId);

        if (!personRepository.existsById(personId)) {
            log.warn("Person not found with id: {}", personId);
            throw new PersonNotFoundException("Person tapılmadı! ID: " + personId);
        }

        List<PersonActivityResponseDTO> activities = personActivityRepository
                .findByPersonIdOrderByPerformedAtDesc(personId)
                .stream()
                .map(personMapper::toActivityResponseDTO)
                .toList();

        log.info("Retrieved {} activities for person id: {}", activities.size(), personId);
        return activities;
    }

    @Override
    public void deleteActivity(Long activityId) {
        log.info("Deleting activity with id: {}", activityId);

        PersonActivity activity = personActivityRepository.findById(activityId)
                .orElseThrow(() -> {
                    log.warn("Activity not found with id: {}", activityId);
                    return new ActivityNotFoundException("Activity tapılmadı! ID: " + activityId);
                });

        personActivityRepository.delete(activity);
        log.info("Activity deleted successfully. Id: {}", activityId);
    }
}