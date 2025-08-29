//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.PersonService;
//import com.example.Excermol.entity.Person;
//import jakarta.validation.Valid;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/persons")
//public class PersonController {
//    private final PersonService personService;
//
//    public PersonController(PersonService personService) {
//        this.personService = personService;
//    }
//
//    // Bütün şəxsləri əldə et
//    @GetMapping
//    public ResponseEntity<List<Person>> getAllPersons() {
//        List<Person> persons = personService.findAll();
//        return ResponseEntity.ok(persons);
//    }
//
//    // İD-yə görə şəxs tap
//    @GetMapping("/{id}")
//    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
//        return personService.findById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // Yeni şəxs yarat
//    @PostMapping
//    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
//        Person savedPerson = personService.save(person);
//        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
//    }
//
//    // Var olan şəxsi yenilə
//    @PutMapping("/{id}")
//    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @Valid @RequestBody Person person) {
//        if (!personService.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        person.setId(id);
//        Person updatedPerson = personService.save(person);
//        return ResponseEntity.ok(updatedPerson);
//    }
//
//    // Şəxsi sil
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
//        if (!personService.existsById(id)) {
//            return ResponseEntity.notFound().build();
//        }
//        personService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
//
//
//}
