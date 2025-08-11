package com.example.Excermol.repository;

import com.example.Excermol.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    List<Organization> findByNameContainingIgnoreCase(String name);

}
