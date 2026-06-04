package com.example.Excermol.repository;

import com.example.Excermol.entity.Form;
import com.example.Excermol.enums.FormStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    // owner-a gore butun formlar
    List<Form> findAllByOwnerId(Long ownerId);

    // statusa gore formlar - PUBLISHED, DRAFT
    List<Form> findAllByStatus(FormStatus status);

    // owner ve statusa gore
    List<Form> findAllByOwnerIdAndStatus(Long ownerId, FormStatus status);

    // adina gore axtaris
    List<Form> findAllByFormsNameContainingIgnoreCase(String name);
}
