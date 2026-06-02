package com.example.Excermol.repository;

import com.example.Excermol.entity.Person;
import com.example.Excermol.enums.PersonStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    // Email unikal olduğu üçün
    Optional<Person> findByEmail(String email);

    // Email mövcuddurmu
    boolean existsByEmail(String email);

    // Statusa görə filterlə — UI-da Status sütunu var
    List<Person> findByStatus(PersonStatus status);

    // Şirkətə görə filterlə
    List<Person> findByCompanyId(Long companyId);



    // User ← new changes
    List<Person> findByUserId(Long userId);
    List<Person> findByUserIdAndStatus(Long userId, PersonStatus status);


}
