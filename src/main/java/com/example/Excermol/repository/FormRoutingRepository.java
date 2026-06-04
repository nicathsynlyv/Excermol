package com.example.Excermol.repository;


import com.example.Excermol.entity.FormRouting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormRoutingRepository extends JpaRepository<FormRouting, Long> {


    // forma gore butun routingler - sirali
    List<FormRouting> findAllByFormIdOrderByRoutingOrderAsc(Long formId);

    // field-e gore routingler
    List<FormRouting> findAllByFormFieldId(Long formFieldId);
}