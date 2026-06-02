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

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
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
        if (personRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Bu email artıq mövcuddur: " + requestDTO.getEmail());
        }

        Person person = personMapper.toEntity(requestDTO);


        if (requestDTO.getCompanyId() != null) {
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(
                            "Company tapılmadı! ID: " + requestDTO.getCompanyId()));
            person.setCompany(company);
        }

        // User ← new changes
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User tapılmadı! ID: " + requestDTO.getUserId()));
            person.setUser(user);
        }

        if (requestDTO.getTagIds() != null && !requestDTO.getTagIds().isEmpty()) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(requestDTO.getTagIds()));
            person.setTags(tags);
        }

        Person saved = personRepository.save(person);

        // Activity avtomatik əlavə et
        PersonActivityRequestDTO activityDTO = new PersonActivityRequestDTO();
        activityDTO.setAction(ActivityAction.CREATED);
        activityDTO.setPerformedBy(saved.getFullName() + " " + saved.getLastName());
        activityDTO.setPersonId(saved.getId());
        personActivityService.addActivity(activityDTO);

        return personMapper.toResponseDTO(saved);
    }

    @Override
    public List<PersonResponseDTO> getAllPersons() {
        return personRepository.findAll()
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }

    @Override
    public PersonResponseDTO getPersonById(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(
                        "Person tapılmadı! ID: " + id));
        return personMapper.toResponseDTO(person);
    }
    @Override
    public PersonResponseDTO updatePerson(Long id, PersonRequestDTO requestDTO) {

        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(
                        "Person tapılmadı! ID: " + id));

        Optional<Person> existingPerson =
                personRepository.findByEmail(requestDTO.getEmail());

        if (existingPerson.isPresent()
                && !existingPerson.get().getId().equals(id)) {

            throw new EmailAlreadyExistsException(
                    "Bu email artıq mövcuddur: " + requestDTO.getEmail());
        }

        personMapper.updateEntity(person, requestDTO);

        if (requestDTO.getCompanyId() != null) {
            Company company = companyRepository.findById(requestDTO.getCompanyId())
                    .orElseThrow(() -> new CompanyNotFoundException(
                            "Company tapılmadı! ID: " + requestDTO.getCompanyId()));
            person.setCompany(company);
        }

        if (requestDTO.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>(tagRepository.findAllById(requestDTO.getTagIds()));
            person.setTags(tags);
        }


        // User ← new changes
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User tapılmadı! ID: " + requestDTO.getUserId()));
            person.setUser(user);
        }

        person.setLastInteractionAt(LocalDateTime.now());

        Person saved = personRepository.save(person);

        PersonActivityRequestDTO activityDTO = new PersonActivityRequestDTO();
        activityDTO.setAction(ActivityAction.UPDATED);
        activityDTO.setPerformedBy(saved.getFullName() + " " + saved.getLastName());
        activityDTO.setPersonId(saved.getId());

        personActivityService.addActivity(activityDTO);

        return personMapper.toResponseDTO(saved);
    }
    @Override
    public void deletePerson(Long id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(
                        "Person tapılmadı! ID: " + id));
        personRepository.delete(person);
    }



//user new changes
    @Override
    public List<PersonResponseDTO> getPersonsByUser(Long userId) {
        return personRepository.findByUserId(userId)
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }
//user new changes
    @Override
    public List<PersonResponseDTO> getPersonsByUserAndStatus(Long userId, PersonStatus status) {
        return personRepository.findByUserIdAndStatus(userId, status)
                .stream()
                .map(personMapper::toResponseDTO)
                .toList();
    }
}
