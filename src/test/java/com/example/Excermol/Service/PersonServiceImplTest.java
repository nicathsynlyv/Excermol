package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.PersonServiceImpl;
import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.Tag;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.PersonActivityRequestDTO;
import com.example.Excermol.entity.dtos.PersonRequestDTO;
import com.example.Excermol.entity.dtos.PersonResponseDTO;
import com.example.Excermol.enums.ActivityAction;
import com.example.Excermol.enums.PersonStatus;
import com.example.Excermol.exception.CompanyNotFoundException;
import com.example.Excermol.exception.EmailAlreadyExistsException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.PersonMapper;
import com.example.Excermol.repository.CompanyRepository;
import com.example.Excermol.repository.PersonRepository;
import com.example.Excermol.repository.TagRepository;
import com.example.Excermol.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonServiceImplTest Unit Test")
public class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private PersonMapper personMapper;
    @Mock
    private PersonActivityService personActivityService;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person;
    private PersonRequestDTO requestDTO;
    private PersonResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        person = new Person();
        person.setId(1L);
        person.setFullName("John");
        person.setLastName("Doe");
        person.setEmail("john@example.com");
        person.setStatus(PersonStatus.ENGAGED);

        requestDTO = new PersonRequestDTO();
        requestDTO.setFullName("John");
        requestDTO.setLastName("Doe");
        requestDTO.setEmail("john@example.com");
        requestDTO.setStatus(PersonStatus.ENGAGED);

        responseDTO = new PersonResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setFullName("John");
        responseDTO.setEmail("john@example.com");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createPerson_shouldSaveAndReturnPerson_withoutRelations() {
        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        PersonResponseDTO result = personService.createPerson(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("john@example.com");
        verify(personRepository).save(person);
    }

    @Test
    void createPerson_shouldThrowException_whenEmailAlreadyExists() {
        when(personRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> personService.createPerson(requestDTO));
        verify(personRepository, never()).save(any());
    }

    @Test
    void createPerson_shouldSetCompany_whenCompanyIdProvided() {
        requestDTO.setCompanyId(10L);
        Company company = new Company();
        company.setId(10L);

        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.createPerson(requestDTO);

        assertThat(person.getCompany()).isEqualTo(company);
        verify(companyRepository).findById(10L);
    }

    @Test
    void createPerson_shouldThrowException_whenCompanyNotFound() {
        requestDTO.setCompanyId(10L);

        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(companyRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> personService.createPerson(requestDTO));
        verify(personRepository, never()).save(any());
    }

    @Test
    void createPerson_shouldSetUser_whenUserIdProvided() {
        requestDTO.setUserId(20L);
        User user = new User();
        user.setId(20L);

        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.createPerson(requestDTO);

        assertThat(person.getUser()).isEqualTo(user);
    }

    @Test
    void createPerson_shouldThrowException_whenUserNotFound() {
        requestDTO.setUserId(20L);

        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(userRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> personService.createPerson(requestDTO));
        verify(personRepository, never()).save(any());
    }

    @Test
    void createPerson_shouldSetTags_whenTagIdsProvided() {
        requestDTO.setTagIds(Set.of(30L));
        Tag tag = new Tag();
        tag.setId(30L);

        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(tagRepository.findAllById(requestDTO.getTagIds())).thenReturn(List.of(tag));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.createPerson(requestDTO);

        assertThat(person.getTags()).containsExactly(tag);
    }

    @Test
    void createPerson_shouldNotSetTags_whenTagIdsEmpty() {
        requestDTO.setTagIds(Set.of());

        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.createPerson(requestDTO);

        verify(tagRepository, never()).findAllById(any());
    }

    @Test
    void createPerson_shouldLogActivity_withCorrectActionAndPersonId() {
        when(personRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(personMapper.toEntity(requestDTO)).thenReturn(person);
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.createPerson(requestDTO);

        ArgumentCaptor<PersonActivityRequestDTO> captor = ArgumentCaptor.forClass(PersonActivityRequestDTO.class);
        verify(personActivityService).addActivity(captor.capture());

        PersonActivityRequestDTO captured = captor.getValue();
        assertThat(captured.getAction()).isEqualTo(ActivityAction.CREATED);
        assertThat(captured.getPersonId()).isEqualTo(1L);
        assertThat(captured.getPerformedBy()).isEqualTo("John Doe");
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAllPersons_shouldReturnAllPersons() {
        when(personRepository.findAll()).thenReturn(List.of(person));
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        List<PersonResponseDTO> result = personService.getAllPersons();

        assertThat(result).hasSize(1);
        verify(personRepository).findAll();
    }

    @Test
    void getAllPersons_shouldReturnEmptyList_whenNoPersons() {
        when(personRepository.findAll()).thenReturn(List.of());

        List<PersonResponseDTO> result = personService.getAllPersons();

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getPersonById_shouldReturnPerson_whenExists() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        PersonResponseDTO result = personService.getPersonById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(personRepository).findById(1L);
    }

    @Test
    void getPersonById_shouldThrowException_whenNotFound() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.getPersonById(99L));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updatePerson_shouldUpdateAndReturnPerson() {
        requestDTO.setFullName("Updated John");

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        PersonResponseDTO result = personService.updatePerson(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(personMapper).updateEntity(person, requestDTO);
        verify(personRepository).save(person);
    }

    @Test
    void updatePerson_shouldThrowException_whenPersonNotFound() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.updatePerson(99L, requestDTO));
        verify(personRepository, never()).save(any());
    }

    @Test
    void updatePerson_shouldThrowException_whenEmailBelongsToAnotherPerson() {
        requestDTO.setEmail("taken@example.com");

        Person otherPerson = new Person();
        otherPerson.setId(2L);
        otherPerson.setEmail("taken@example.com");

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("taken@example.com")).thenReturn(Optional.of(otherPerson));

        assertThrows(EmailAlreadyExistsException.class, () -> personService.updatePerson(1L, requestDTO));
        verify(personRepository, never()).save(any());
    }

    @Test
    void updatePerson_shouldAllowUpdate_whenEmailBelongsToSamePerson() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        PersonResponseDTO result = personService.updatePerson(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(personRepository).save(person);
    }

    @Test
    void updatePerson_shouldAllowUpdate_whenEmailNotFoundInRepository() {
        requestDTO.setEmail("new@example.com");

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("new@example.com")).thenReturn(Optional.empty());
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        PersonResponseDTO result = personService.updatePerson(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(personRepository).save(person);
    }

    @Test
    void updatePerson_shouldUpdateCompany_whenCompanyIdProvided() {
        requestDTO.setCompanyId(10L);
        Company company = new Company();
        company.setId(10L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(companyRepository.findById(10L)).thenReturn(Optional.of(company));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.updatePerson(1L, requestDTO);

        assertThat(person.getCompany()).isEqualTo(company);
    }

    @Test
    void updatePerson_shouldThrowException_whenCompanyNotFound() {
        requestDTO.setCompanyId(10L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(companyRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> personService.updatePerson(1L, requestDTO));
        verify(personRepository, never()).save(any());
    }

    @Test
    void updatePerson_shouldUpdateTags_whenTagIdsProvided() {
        requestDTO.setTagIds(Set.of(30L));
        Tag tag = new Tag();
        tag.setId(30L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(tagRepository.findAllById(requestDTO.getTagIds())).thenReturn(List.of(tag));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.updatePerson(1L, requestDTO);

        assertThat(person.getTags()).containsExactly(tag);
    }

    @Test
    void updatePerson_shouldUpdateUser_whenUserIdProvided() {
        requestDTO.setUserId(20L);
        User user = new User();
        user.setId(20L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.updatePerson(1L, requestDTO);

        assertThat(person.getUser()).isEqualTo(user);
    }

    @Test
    void updatePerson_shouldThrowException_whenUserNotFound() {
        requestDTO.setUserId(20L);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(userRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> personService.updatePerson(1L, requestDTO));
        verify(personRepository, never()).save(any());
    }

    @Test
    void updatePerson_shouldUpdateLastInteractionAt() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.updatePerson(1L, requestDTO);

        assertThat(person.getLastInteractionAt()).isNotNull();
    }

    @Test
    void updatePerson_shouldLogActivity_withCorrectActionAndPersonId() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(personRepository.findByEmail("john@example.com")).thenReturn(Optional.of(person));
        when(personRepository.save(person)).thenReturn(person);
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        personService.updatePerson(1L, requestDTO);

        ArgumentCaptor<PersonActivityRequestDTO> captor = ArgumentCaptor.forClass(PersonActivityRequestDTO.class);
        verify(personActivityService).addActivity(captor.capture());

        PersonActivityRequestDTO captured = captor.getValue();
        assertThat(captured.getAction()).isEqualTo(ActivityAction.UPDATED);
        assertThat(captured.getPersonId()).isEqualTo(1L);
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deletePerson_shouldDeletePerson_whenExists() {
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        personService.deletePerson(1L);

        verify(personRepository).delete(person);
    }

    @Test
    void deletePerson_shouldThrowException_whenNotFound() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.deletePerson(99L));
        verify(personRepository, never()).delete(any());
    }

    // =========================
    // GET BY USER
    // =========================
    @Test
    void getPersonsByUser_shouldReturnFilteredPersons() {
        when(personRepository.findByUserId(20L)).thenReturn(List.of(person));
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        List<PersonResponseDTO> result = personService.getPersonsByUser(20L);

        assertThat(result).hasSize(1);
        verify(personRepository).findByUserId(20L);
    }

    @Test
    void getPersonsByUser_shouldReturnEmptyList_whenNoPersons() {
        when(personRepository.findByUserId(20L)).thenReturn(List.of());

        List<PersonResponseDTO> result = personService.getPersonsByUser(20L);

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY USER AND STATUS
    // =========================
    @Test
    void getPersonsByUserAndStatus_shouldReturnFilteredPersons() {
        when(personRepository.findByUserIdAndStatus(20L, PersonStatus.ENGAGED)).thenReturn(List.of(person));
        when(personMapper.toResponseDTO(person)).thenReturn(responseDTO);

        List<PersonResponseDTO> result = personService.getPersonsByUserAndStatus(20L, PersonStatus.ENGAGED);

        assertThat(result).hasSize(1);
        verify(personRepository).findByUserIdAndStatus(20L, PersonStatus.ENGAGED);
    }

    @Test
    void getPersonsByUserAndStatus_shouldReturnEmptyList_whenNoMatches() {
        when(personRepository.findByUserIdAndStatus(20L, PersonStatus.ENGAGED)).thenReturn(List.of());

        List<PersonResponseDTO> result = personService.getPersonsByUserAndStatus(20L, PersonStatus.ENGAGED);

        assertThat(result).isEmpty();
    }


}
