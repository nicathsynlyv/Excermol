package com.example.Excermol.repository;

import com.example.Excermol.entity.Person;
import com.example.Excermol.enums.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Status-a görə adamları tapmaq
    List<Person> findByStatus(PersonStatus status);

    // Adına görə axtarış (case-insensitive)
    List<Person> findByFullNameContainingIgnoreCase(String fullName);


}
