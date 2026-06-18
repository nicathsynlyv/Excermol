package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.CompanyService;
import com.example.Excermol.entity.Company;
import com.example.Excermol.entity.Person;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.CompanyRequestDTO;
import com.example.Excermol.entity.dtos.CompanyResponseDTO;
import com.example.Excermol.enums.CompanyStatus;
import com.example.Excermol.exception.CompanyNotFoundException;
import com.example.Excermol.exception.DomainAlreadyExistsException;
import com.example.Excermol.exception.PersonNotFoundException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.CompanyMapper;
import com.example.Excermol.repository.CompanyRepository;
import com.example.Excermol.repository.PersonRepository;
import com.example.Excermol.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyMapper companyMapper;
    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository,
                              CompanyMapper companyMapper,
                              PersonRepository personRepository,
                              UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.companyMapper = companyMapper;
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }


    @Override
    public CompanyResponseDTO createCompany(CompanyRequestDTO requestDTO) {
        log.info("Creating company with name: {}", requestDTO.getCompanyName());
        // Domain duplicate yoxlaması
        if (companyRepository.findByDomain(requestDTO.getDomain()).isPresent()) {
            log.warn("Domain already exists: {}", requestDTO.getDomain());
            throw new DomainAlreadyExistsException("Bu domain artıq mövcuddur: " + requestDTO.getDomain());
        }

        Company company = companyMapper.toEntity(requestDTO);

        // Owner set et
        if (requestDTO.getOwnerId() != null) {
            Person owner = personRepository.findById(requestDTO.getOwnerId())
                    .orElseThrow(() -> {
                        log.warn("Person not found with id: {}", requestDTO.getOwnerId());
                        return new PersonNotFoundException("Person tapılmadı! ID: " + requestDTO.getOwnerId());
                    });
            company.setOwner(owner);
        }

        // User ← new changes
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> {
                        log.warn("User not found with id: {}", requestDTO.getUserId());
                        return new UserNotFoundException("User tapılmadı! ID: " + requestDTO.getUserId());
                    });
            company.setUser(user);
        }

        Company saved = companyRepository.save(company);
        log.info("Company created successfully with id: {}", saved.getId());
        return companyMapper.toResponseDTO(saved);
    }

    @Override
    public Page<CompanyResponseDTO> getAllCompanies(Pageable pageable) {
        log.info("Fetching all companies - page {}, size {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Company> companies = companyRepository.findAll(pageable);
        log.info("Retrieved {} companies", companies.getTotalElements());
        return companies.map(companyMapper::toResponseDTO);
    }

    @Override
    public CompanyResponseDTO getCompanyById(Long id) {
        log.info("Fetching company with id: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Company not found with id: {}", id);
                    return new CompanyNotFoundException("Company tapılmadı! ID: " + id);
                });
        log.info("Company found with id: {}", id);
        return companyMapper.toResponseDTO(company);
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO) {
        log.info("Updating company with id: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Company not found for update. Id: {}", id);
                    return new CompanyNotFoundException("Company tapılmadı! ID: " + id);
                });

        // Domain dəyişirsə — duplicate yoxla
        if (!company.getDomain().equals(requestDTO.getDomain())) {
            log.info("Domain changed for company id: {}. Checking duplicate...", id);
            if (companyRepository.findByDomain(requestDTO.getDomain()).isPresent()) {
                log.warn("Domain already exists: {}", requestDTO.getDomain());
                throw new DomainAlreadyExistsException("Bu domain artıq mövcuddur: " + requestDTO.getDomain());
            }


            // User ← new changes
            if (requestDTO.getUserId() != null) {
                User user = userRepository.findById(requestDTO.getUserId())
                        .orElseThrow(() -> {
                            log.warn("User not found with id: {}", requestDTO.getUserId());
                            return new UserNotFoundException("User tapılmadı! ID: " + requestDTO.getUserId());
                        });
                company.setUser(user);
            }
        }

        companyMapper.updateEntity(company, requestDTO);

        // Owner yenilə
        if (requestDTO.getOwnerId() != null) {
            Person owner = personRepository.findById(requestDTO.getOwnerId())
                    .orElseThrow(() -> {
                        log.warn("Person not found with id: {}", requestDTO.getOwnerId());
                        return new PersonNotFoundException("Person tapılmadı! ID: " + requestDTO.getOwnerId());
                    });
            company.setOwner(owner);
        }

        Company saved = companyRepository.save(company);
        log.info("Company updated successfully. Id: {}", id);
        return companyMapper.toResponseDTO(saved);
    }

    @Override
    public void deleteCompany(Long id) {
        log.info("Deleting company with id: {}", id);
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Company not found for deletion. Id: {}", id);
                    return new CompanyNotFoundException("Company tapılmadı! ID: " + id);
                });
        companyRepository.delete(company);
        log.info("Company deleted successfully. Id: {}", id);
    }

    @Override
    public List<CompanyResponseDTO> getCompaniesByStatus(CompanyStatus status) {
        log.info("Fetching companies by status: {}", status);
        return companyMapper.toResponseList(companyRepository.findByStatus(status));
    }

    @Override
    public List<CompanyResponseDTO> getCompaniesByOwner(Long ownerId) {
        log.info("Fetching companies by owner id: {}", ownerId);
        return companyMapper.toResponseList(companyRepository.findByOwnerId(ownerId));
    }

    @Override
    public List<CompanyResponseDTO> searchCompanies(String companyName) {
        log.info("Searching companies with name: {}", companyName);
        List<Company> companies =
                companyRepository.findByCompanyNameContainingIgnoreCase(companyName);
        log.info("Found {} companies matching '{}'", companies.size(), companyName);
        return companyMapper.toResponseList(companies);
    }


    //user new changes
    @Override
    public List<CompanyResponseDTO> getCompaniesByUser(Long userId) {
        log.info("Fetching companies by user id: {}", userId);
        return companyMapper.toResponseList(companyRepository.findByUserId(userId));
    }

    //user new changes
    @Override
    public List<CompanyResponseDTO> getCompaniesByUserAndStatus(Long userId, CompanyStatus status) {
        log.info("Fetching companies by user id: {} and status: {}", userId, status);
        return companyMapper.toResponseList(
                companyRepository.findByUserIdAndStatus(userId, status));
    }
}

