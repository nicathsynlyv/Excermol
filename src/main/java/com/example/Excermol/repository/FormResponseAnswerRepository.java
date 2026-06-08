package com.example.Excermol.repository;

import com.example.Excermol.entity.FormResponseAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FormResponseAnswerRepository extends JpaRepository<FormResponseAnswer, Long> {

    // response-a gore butun cavablar
    List<FormResponseAnswer> findAllByFormResponseId(Long formResponseId);

    // field-e gore butun cavablar
    List<FormResponseAnswer> findAllByFormFieldId(Long formFieldId);
}
