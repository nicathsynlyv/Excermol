package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CompanyService;
import com.example.Excermol.entity.Company;
import com.example.Excermol.enums.CompanyStatus;
import com.example.Excermol.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    // Constructor Injection (ən tövsiyə olunan üsul)
    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    @Override
    public Optional<Company> getById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company save(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public void deleteById(Long id) {
        companyRepository.deleteById(id);
    }

    // Əlavə olaraq sənin repository-də olan xüsusi metodlar:
    public Optional<Company> getByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }

    public Optional<Company> getByDomain(String domain) {
        return companyRepository.findByDomain(domain);
    }

    public List<Company> getByStatus(CompanyStatus status) {
        return companyRepository.findByStatus(status);
    }
}

