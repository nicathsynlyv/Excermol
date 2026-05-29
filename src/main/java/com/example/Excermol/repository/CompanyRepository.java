package com.example.Excermol.repository;

import com.example.Excermol.entity.Company;
import com.example.Excermol.enums.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByCompanyNameContainingIgnoreCase(String companyName);

    Optional<Company> findByDomain(String domain);
    List<Company> findByStatus(CompanyStatus status);
    List<Company> findByOwnerId(Long ownerId);

    // ✅ Pagination ilə filter — böyük data olduqda lazım olur
    Page<Company> findByStatus(CompanyStatus status, Pageable pageable);
    Page<Company> findByOwnerId(Long ownerId, Pageable pageable);
}
