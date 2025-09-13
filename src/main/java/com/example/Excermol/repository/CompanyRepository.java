package com.example.Excermol.repository;

import com.example.Excermol.entity.Company;
import com.example.Excermol.enums.CompanyStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    // 1️⃣ CompanyName ilə axtarış (Create və ya UI-də lazım olur)
    Optional<Company> findByCompanyName(String companyName);

    // 2️⃣ Domain ilə axtarış (duplicate check üçün əhəmiyyətli)
    Optional<Company> findByDomain(String domain);

    // 3️⃣ Status ilə filter (Closed, Interested, Engaged və s. üçün)
    List<Company> findByStatus(CompanyStatus status);
}
