package com.example.Excermol.repository;


import com.example.Excermol.entity.FormField;
import com.example.Excermol.enums.FieldType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FormFieldRepository extends JpaRepository<FormField, Long> {
    // forma gore butun fieldler - sirali
    List<FormField> findAllByFormIdOrderByFieldOrderAsc(Long formId);

    // field tipine gore
    List<FormField> findAllByFormIdAndFieldType(Long formId, FieldType fieldType);
}
