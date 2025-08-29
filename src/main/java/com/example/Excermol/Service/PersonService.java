//package com.example.Excermol.Service;
//
//import com.example.Excermol.entity.Person;
//import com.example.Excermol.repository.PersonRepository;
//import jakarta.validation.Valid;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class PersonService {
//    private final PersonRepository personRepository;
//
//    public PersonService(PersonRepository personRepository) {
//        this.personRepository = personRepository;
//    }
//
//    public List<Person> findAll() {
//        return personRepository.findAll();
//    }
//
//    public Optional<Person> findById(Long id) {
//        return personRepository.findById(id);
//    }
//
//    public Person save(@Valid Person person) {
//        return personRepository.save(person);
//    }
//
//    public void deleteById(Long id) {
//        personRepository.deleteById(id);
//    }
//
//    public boolean existsById(Long id) {
//        return personRepository.existsById(id);
//    }
//
//}
