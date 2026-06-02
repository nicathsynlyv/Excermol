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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Transactional
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
        // Domain duplicate yoxlaması
        if (companyRepository.findByDomain(requestDTO.getDomain()).isPresent()) {
            throw new DomainAlreadyExistsException("Bu domain artıq mövcuddur: " + requestDTO.getDomain());
        }

        Company company = companyMapper.toEntity(requestDTO);

        // Owner set et
        if (requestDTO.getOwnerId() != null) {
            Person owner = personRepository.findById(requestDTO.getOwnerId())
                    .orElseThrow(() -> new PersonNotFoundException(
                            "Person tapılmadı! ID: " + requestDTO.getOwnerId()));
            company.setOwner(owner);
        }

        // User ← new changes
        if (requestDTO.getUserId() != null) {
            User user = userRepository.findById(requestDTO.getUserId())
                    .orElseThrow(() -> new UserNotFoundException(
                            "User tapılmadı! ID: " + requestDTO.getUserId()));
            company.setUser(user);
        }

        Company saved = companyRepository.save(company);
        return companyMapper.toResponseDTO(saved);
    }

    @Override
    public Page<CompanyResponseDTO> getAllCompanies(Pageable pageable) {

        Page<Company> companies = companyRepository.findAll(pageable);

        return companies.map(companyMapper::toResponseDTO);
    }

    @Override
    public CompanyResponseDTO getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company tapılmadı! ID: " + id));
        return companyMapper.toResponseDTO(company);
    }

    @Override
    public CompanyResponseDTO updateCompany(Long id, CompanyRequestDTO requestDTO) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company tapılmadı! ID: " + id));

        // Domain dəyişirsə — duplicate yoxla
        if (!company.getDomain().equals(requestDTO.getDomain())) {
            if (companyRepository.findByDomain(requestDTO.getDomain()).isPresent()) {
                throw new DomainAlreadyExistsException("Bu domain artıq mövcuddur: " + requestDTO.getDomain());
            }


            // User ← new changes
            if (requestDTO.getUserId() != null) {
                User user = userRepository.findById(requestDTO.getUserId())
                        .orElseThrow(() -> new UserNotFoundException(
                                "User tapılmadı! ID: " + requestDTO.getUserId()));
                company.setUser(user);
            }
        }

        companyMapper.updateEntity(company, requestDTO);

        // Owner yenilə
        if (requestDTO.getOwnerId() != null) {
            Person owner = personRepository.findById(requestDTO.getOwnerId())
                    .orElseThrow(() -> new PersonNotFoundException(
                            "Person tapılmadı! ID: " + requestDTO.getOwnerId()));
            company.setOwner(owner);
        }

        Company saved = companyRepository.save(company);
        return companyMapper.toResponseDTO(saved);
    }

    @Override
    public void deleteCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new CompanyNotFoundException(
                        "Company tapılmadı! ID: " + id));
        companyRepository.delete(company);
    }

    @Override
    public List<CompanyResponseDTO> getCompaniesByStatus(CompanyStatus status) {
        return companyMapper.toResponseList(companyRepository.findByStatus(status));
    }

    @Override
    public List<CompanyResponseDTO> getCompaniesByOwner(Long ownerId) {
        return companyMapper.toResponseList(companyRepository.findByOwnerId(ownerId));
    }

    @Override
    public List<CompanyResponseDTO> searchCompanies(String companyName) {

        List<Company> companies =
                companyRepository.findByCompanyNameContainingIgnoreCase(companyName);

        return companyMapper.toResponseList(companies);
    }


//user new changes
    @Override
    public List<CompanyResponseDTO> getCompaniesByUser(Long userId) {
        return companyMapper.toResponseList(companyRepository.findByUserId(userId));
    }
//user new changes
    @Override
    public List<CompanyResponseDTO> getCompaniesByUserAndStatus(Long userId, CompanyStatus status) {
        return companyMapper.toResponseList(
                companyRepository.findByUserIdAndStatus(userId, status));
    }
}

