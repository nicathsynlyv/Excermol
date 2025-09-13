package com.example.Excermol.repository;

import com.example.Excermol.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {

    // Response sayını hesabla
    long countByFormId(Long formId);


}