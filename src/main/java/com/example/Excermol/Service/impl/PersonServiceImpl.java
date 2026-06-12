package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.PersonActivityService;
import com.example.Excermol.Service.PersonService;
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
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;
    private final CompanyRepository companyRepository;
    private final TagRepository tagRepository;
    private final PersonMapper personMapper;
    private final PersonActivityService personActivityService;
    private final UserRepository userRepository;

    public PersonServiceImpl(PersonRepository personRepository,
                             CompanyRepository companyRepository,
                             TagRepository tagRepository,
                             PersonMapper personMapper,
                             PersonActivityService personActivityService,
                             UserRepository userRepository) {
        this.personRepository = personRepository;
        this.companyRepository = companyRepository;
        this.tagRepository = tagRepository;
        this.personMapper = personMapper;
        this.personActivityService = personActivityService;
        this.userRepository = userRepository;
    }

    @Override
    public PersonResponseDTO createPerson(PersonRequestDTO requestDTO) {
        log.info("Creating person with email: {}", requestDTO.getEmail());

        if (personRepository.existsByEmail(requestDTO.getEmail())) {
            log.warn("Email already exists: {}", requestDTO.getEmail());
            throw new EmailAlreadyExistsException("Bu email artıq mövcuddur: " + requestDTO.getEmail());
        }

        Person person = personMapper.toEntity(requestDTO);

        if (requestDTO.getCompanyId() != null) {
            log.info("Setting company with id: {}", requestDTO.getCompanyId());
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> {
                        log.warn("Company not found with id: {}", requestDTO.getCompanyId());
                        return new CompanyNotFoundException("Company tapılmadı! ID: " + requestDTO.getCompanyId());
                    });
            person.setCompany(company);
        }

        if (requestDTO.getUserId() != null) {
            log.info("Setting user with id: {}", requestDTO.getUserId());
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        log.warn("User not found with id: {}", requestDTO.getUserId());
                        return new UserNotFoundException("User tapılmadı! ID: " + requestDTO.getUserId());
                    });
            person.setUser(user);
        }

        if (requestDTO.getTagIds() != null && !requestDTO.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(requestDTO.getTagIds()));
            person.setTags(tags);
        }

        Person saved = personRepository.save(person);
        log.info("Person created successfully with id: {}", saved.getId());

        PersonActivityRequestDTO activityDTO = new PersonActivityRequestDTO();
        activityDTO.setAction(ActivityAction.CREATED);
        activityDTO.setPerformedBy(saved.getFullName() + " " + saved.getLastName());
        activityDTO.setPersonId(saved.getId());
        personActivityService.addActivity(activityDTO);
        log.info("Activity logged for person id: {}", saved.getId());

        return personMapper.toResponseDTO(saved);
    }

    @Override
    public List<PersonResponseDTO> getAllPersons() {
        log.info("Fetching all persons");

        List<PersonResponseDTO> persons = personRepository.findAll()
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();

        log.info("Retrieved {} persons", persons.size());
        return persons;
    }

    @Override
    public PersonResponseDTO getPersonById(Long id) {
        log.info("Fetching person with id: {}", id);

        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Person not found with id: {}", id);
                    return new PersonNotFoundException("Person tapılmadı! ID: " + id);
                });

        log.info("Person found with id: {}", id);
        return personMapper.toResponseDTO(person);
    }

    @Override
    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO requestDTO) {
        log.info("Updating person with id: {}", id);

        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Person not found for update. Id: {}", id);
                    return new PersonNotFoundException("Person tapılmadı! ID: " + id);
                });

        Optional<Person> existingPerson = personRepository.findByEmail(requestDTO.getEmail());

        if (existingPerson.isPresent() && !existingPerson.get().getId().equals(id)) {
            log.warn("Email already exists: {}", requestDTO.getEmail());
            throw new EmailAlreadyExistsException("Bu email artıq mövcuddur: " + requestDTO.getEmail());
        }

        personMapper.updateEntity(person, requestDTO);

        if (requestDTO.getCompanyId() != null) {
            log.info("Updating company with id: {}", requestDTO.getCompanyId());
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> {
                        log.warn("Company not found with id: {}", requestDTO.getCompanyId());
                        return new CompanyNotFoundException("Company tapılmadı! ID: " + requestDTO.getCompanyId());
                    });
            person.setCompany(company);
        }

        if (requestDTO.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(requestDTO.getTagIds()));
            person.setTags(tags);
        }

        if (requestDTO.getUserId() != null) {
            log.info("Updating user with id: {}", requestDTO.getUserId());
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        log.warn("User not found with id: {}", requestDTO.getUserId());
                        return new UserNotFoundException("User tapılmadı! ID: " + requestDTO.getUserId());
                    });
            person.setUser(user);
        }

        person.setLastInteractionAt(LocalDateTime.now());

        Person saved = personRepository.save(person);
        log.info("Person updated successfully. Id: {}", id);

        PersonActivityRequestDTO activityDTO = new PersonActivityRequestDTO();
        activityDTO.setAction(ActivityAction.UPDATED);
        activityDTO.setPerformedBy(saved.getFullName() + " " + saved.getLastName());
        activityDTO.setPersonId(saved.getId());
        personActivityService.addActivity(activityDTO);
        log.info("Activity logged for person id: {}", saved.getId());

        return personMapper.toResponseDTO(saved);
    }

    @Override
    public void deletePerson(Long id) {
        log.info("Deleting person with id: {}", id);

        Person person = personRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Person not found for deletion. Id: {}", id);
                    return new PersonNotFoundException("Person tapılmadı! ID: " + id);
                });

        personRepository.delete(person);
        log.info("Person deleted successfully. Id: {}", id);
    }

    @Override
    public List<PersonResponseDTO> getPersonsByUser(Long userId) {
        log.info("Fetching persons for user id: {}", userId);

        List<PersonResponseDTO> persons = personRepository.findByUserId(userId)
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();

        log.info("Retrieved {} persons for user id: {}", persons.size(), userId);
        return persons;
    }

    @Override
    public List<PersonResponseDTO> getPersonsByUserAndStatus(Long userId, PersonStatus status) {
        log.info("Fetching persons for user id: {} with status: {}", userId, status);

        List<PersonResponseDTO> persons = personRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();

        log.info("Retrieved {} persons for user id: {} with status: {}", persons.size(), userId, status);
        return persons;
    }
}