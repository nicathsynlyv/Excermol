package com.example.Excermol.repository;

import com.example.Excermol.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    // Forms siyahısını response count ilə birlikdə gətir
    @Query("SELECT f FROM Form f LEFT JOIN FETCH f.responses ORDER BY f.createdAt DESC")
    List<Form> findAllWithResponses();

    // Form adına görə axtarış
    List<Form> findByFormsNameContainingIgnoreCase(String name);

}
