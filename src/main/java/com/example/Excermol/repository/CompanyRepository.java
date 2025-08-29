//package com.example.Excermol.repository;
//
//import com.example.Excermol.entity.Company;
//import com.example.Excermol.enums.CompanyStatus;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//@Repository
//public interface CompanyRepository extends JpaRepository<Company, Long> {
//    List<Company> findByStatus(CompanyStatus status);
//
//    List<Company> findByCityContainingIgnoreCase(String city);
//
//    List<Company> findByOwnerId(Long ownerId);
//
//    List<Company> findByNameContainingIgnoreCase(String name);
//}
