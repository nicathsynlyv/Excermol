package com.example.Excermol.repository;

import com.example.Excermol.entity.Builder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BuilderRepository extends JpaRepository<Builder, Long> {

    // Form ID-yə görə builder tap
    Optional<Builder> findByFormId(Long formId);

    // Form title-a görə axtarış
    Optional<Builder> findByFormTitle(String formTitle);
}
