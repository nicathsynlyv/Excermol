package com.example.Excermol.repository;

import com.example.Excermol.entity.PersonNote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonNoteRepository extends JpaRepository<PersonNote, Long> {
    // Şəxsə aid bütün notları gətir
    List<PersonNote> findByPersonId(Long personId);
}
