package com.example.Excermol.repository;

import com.example.Excermol.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // domain unikal olduğu üçün
    Optional<Organization> findByDomain(String domain);

    // domain artıq mövcuddurmu yoxlamaq üçün
    boolean existsByDomain(String domain);
}
