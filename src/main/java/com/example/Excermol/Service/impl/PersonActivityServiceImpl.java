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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
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
        Person person = personRepository.findById(requestDTO.getPersonId())
                .orElseThrow(() -> new PersonNotFoundException(
                        "Person tapılmadı! ID: " + requestDTO.getPersonId()));

        PersonActivity activity = personMapper.toActivityEntity(requestDTO);
        activity.setPerson(person);

        return personMapper.toActivityResponseDTO(personActivityRepository.save(activity));
    }

    @Override
    public List<PersonActivityResponseDTO> getActivitiesByPersonId(Long personId) {
        if (!personRepository.existsById(personId)) {
            throw new PersonNotFoundException("Person tapılmadı! ID: " + personId);
        }

        return personActivityRepository.findByPersonIdOrderByPerformedAtDesc(personId)
                .stream()
                .map(personMapper::toActivityResponseDTO)
                .toList();
    }

    @Override
    public void deleteActivity(Long activityId) {
        PersonActivity activity = personActivityRepository.findById(activityId)
                .orElseThrow(() -> new ActivityNotFoundException(
                        "Activity tapılmadı! ID: " + activityId));

        personActivityRepository.delete(activity);
    }
}