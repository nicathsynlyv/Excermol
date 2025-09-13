package com.example.Excermol.service;

import com.example.Excermol.Service.PersonService;
import com.example.Excermol.entity.Person;
import com.example.Excermol.enums.PersonStatus;
import com.example.Excermol.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    private final PersonRepository personRepository;

    // ---- BaseService metodları ----

    @Override
    public List<Person> getAll() {
        return personRepository.findAll();
    }

    @Override
    public Optional<Person> getById(Long id) {
        return personRepository.findById(id);
    }

    @Override
    public Person save(Person entity) {
        return personRepository.save(entity);
    }

    @Override
    public void deleteById(Long id) {
        personRepository.deleteById(id);
    }

    // ---- Əlavə metodlar ----

    // Status-a görə adamları tapmaq
    public List<Person> getByStatus(PersonStatus status) {
        return personRepository.findByStatus(status);
    }

    // Adına görə search
    public List<Person> searchByName(String name) {
        return personRepository.findByFullNameContainingIgnoreCase(name);
    }


    // Pagination və sort dəstəyi
    public Page<Person> getAllWithPagination(Pageable pageable) {
        return personRepository.findAll(pageable);
    }
}
