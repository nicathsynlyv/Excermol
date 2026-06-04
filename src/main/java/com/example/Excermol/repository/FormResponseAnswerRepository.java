package com.example.Excermol.repository;

import com.example.Excermol.entity.FormResponseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormResponseAnswerRepository extends JpaRepository<FormResponseAnswer, Long> {

    // response-a gore butun cavablar
    List<FormResponseAnswer> findAllByFormResponseId(Long formResponseId);

    // field-e gore butun cavablar
    List<FormResponseAnswer> findAllByFormFieldId(Long formFieldId);
}
