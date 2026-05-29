package com.example.Excermol.Service;


import com.example.Excermol.entity.dtos.CompanyRequestDTO;
import com.example.Excermol.entity.dtos.CompanyResponseDTO;
import com.example.Excermol.enums.CompanyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CompanyService {



//    company name tapmaq ucun
    List<CompanyResponseDTO> searchCompanies(String companyName);


    // ✅ Yarat
    CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO);

    // ✅ Hamısını gətir
    Page<CompanyResponseDTO> getAllCompanies(Pageable pageable);

    // ✅ ID ilə gətir
    CompanyResponseDTO getCompanyById(Long id);

    // ✅ Yenilə
    CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO);

    // ✅ Sil
    void deleteCompany(Long id);

    // ✅ Status ilə filter
    List<CompanyResponseDTO> getCompaniesByStatus(CompanyStatus status);

    // ✅ Owner-a görə şirkətlər
    List<CompanyResponseDTO> getCompaniesByOwner(Long ownerId);
}
