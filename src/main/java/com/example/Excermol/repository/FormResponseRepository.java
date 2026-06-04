package com.example.Excermol.repository;

import com.example.Excermol.entity.FormResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormResponseRepository extends JpaRepository<FormResponse, Long> {

    // forma gore butun cavablar
    List<FormResponse> findAllByFormId(Long formId);

    // contakta gore cavablar
    List<FormResponse> findAllByContactId(Long contactId);

    // forma gore cavab sayi
    Integer countByFormId(Long formId);


}