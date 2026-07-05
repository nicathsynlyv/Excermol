package com.example.Excermol.Service;
import com.example.Excermol.Service.impl.PersonActivityServiceImpl;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.PersonActivity;
import com.example.Excermol.entity.dtos.PersonActivityRequestDTO;
import com.example.Excermol.entity.dtos.PersonActivityResponseDTO;
import com.example.Excermol.enums.ActivityAction;
import com.example.Excermol.exception.ActivityNotFoundException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.mapper.PersonMapper;
import com.example.Excermol.repository.PersonActivityRepository;
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
@DisplayName("PersonActivityServiceImplTest Unit Tests")
public class PersonActivityServiceImplTest {
    @Mock
    private PersonActivityRepository personActivityRepository;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonMapper personMapper;

    @InjectMocks
    private PersonActivityServiceImpl personActivityService;

    private Person person;
    private PersonActivity activity;
    private PersonActivityRequestDTO requestDTO;
    private PersonActivityResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setFullName("John");
        person.setLastName("Doe");

        activity = new PersonActivity();
        activity.setId(10L);
        activity.setAction(ActivityAction.CREATED);
        activity.setPerformedBy("John Doe");
        activity.setPerson(person);

        requestDTO = new PersonActivityRequestDTO();
        requestDTO.setPersonId(1L);
        requestDTO.setAction(ActivityAction.CREATED);
        requestDTO.setPerformedBy("John Doe");

        responseDTO = new PersonActivityResponseDTO();
        responseDTO.setId(10L);
        responseDTO.setAction(ActivityAction.CREATED);
        responseDTO.setPerformedBy("John Doe");
        responseDTO.setPersonId(1L);
    }

    // =========================
    // ADD ACTIVITY
    // =========================
    @Test
    void addActivity_shouldSaveAndReturnActivity() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toActivityEntity(requestDTO)).thenReturn(activity);
        when(personActivityRepository.save(activity)).thenReturn(activity);
        when(personMapper.toActivityResponseDTO(activity)).thenReturn(responseDTO);

        PersonActivityResponseDTO result = personActivityService.addActivity(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getAction()).isEqualTo(ActivityAction.CREATED);
        assertThat(activity.getPerson()).isEqualTo(person);
        verify(personActivityRepository).save(activity);
    }

    @Test
    void addActivity_shouldThrowException_whenPersonNotFound() {
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class,
                () -> personActivityService.addActivity(requestDTO));
        verify(personActivityRepository, never()).save(any());
    }

    @Test
    void addActivity_shouldSetPersonOnActivity() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toActivityEntity(requestDTO)).thenReturn(activity);
        when(personActivityRepository.save(activity)).thenReturn(activity);
        when(personMapper.toActivityResponseDTO(activity)).thenReturn(responseDTO);

        personActivityService.addActivity(requestDTO);

        assertThat(activity.getPerson()).isEqualTo(person);
    }

    @Test
    void addActivity_shouldWork_withUpdatedAction() {
        requestDTO.setAction(ActivityAction.UPDATED);
        activity.setAction(ActivityAction.UPDATED);
        responseDTO.setAction(ActivityAction.UPDATED);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toActivityEntity(requestDTO)).thenReturn(activity);
        when(personActivityRepository.save(activity)).thenReturn(activity);
        when(personMapper.toActivityResponseDTO(activity)).thenReturn(responseDTO);

        PersonActivityResponseDTO result = personActivityService.addActivity(requestDTO);

        assertThat(result.getAction()).isEqualTo(ActivityAction.UPDATED);
    }

    // =========================
    // GET BY PERSON ID
    // =========================
    @Test
    void getActivitiesByPersonId_shouldReturnActivities_whenPersonExists() {
        when(personRepository.existsById(1L)).thenReturn(true);
        when(personActivityRepository.findByPersonIdOrderByPerformedAtDesc(1L))
                .thenReturn(List.of(activity));
        when(personMapper.toActivityResponseDTO(activity)).thenReturn(responseDTO);

        List<PersonActivityResponseDTO> result = personActivityService.getActivitiesByPersonId(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAction()).isEqualTo(ActivityAction.CREATED);
        verify(personActivityRepository).findByPersonIdOrderByPerformedAtDesc(1L);
    }

    @Test
    void getActivitiesByPersonId_shouldReturnEmptyList_whenNoActivities() {
        when(personRepository.existsById(1L)).thenReturn(true);
        when(personActivityRepository.findByPersonIdOrderByPerformedAtDesc(1L))
                .thenReturn(List.of());

        List<PersonActivityResponseDTO> result = personActivityService.getActivitiesByPersonId(1L);

        assertThat(result).isEmpty();
    }

    @Test
    void getActivitiesByPersonId_shouldThrowException_whenPersonNotFound() {
        when(personRepository.existsById(99L)).thenReturn(false);

        assertThrows(PersonNotFoundException.class,
                () -> personActivityService.getActivitiesByPersonId(99L));
        verify(personActivityRepository, never()).findByPersonIdOrderByPerformedAtDesc(any());
    }

    // =========================
    // DELETE ACTIVITY
    // =========================
    @Test
    void deleteActivity_shouldDeleteActivity_whenExists() {
        when(personActivityRepository.findById(10L)).thenReturn(Optional.of(activity));

        personActivityService.deleteActivity(10L);

        verify(personActivityRepository).delete(activity);
    }

    @Test
    void deleteActivity_shouldThrowException_whenNotFound() {
        when(personActivityRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ActivityNotFoundException.class,
                () -> personActivityService.deleteActivity(99L));
        verify(personActivityRepository, never()).delete(any());
    }
}
