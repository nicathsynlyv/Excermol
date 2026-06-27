package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.CompanyServiceImpl;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CompanyServiceImplTest Unit Tests")
class CompanyServiceImplTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CompanyMapper companyMapper;
    @Mock
    private PersonRepository personRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CompanyServiceImpl companyService;

    private Company company;
    private CompanyRequestDTO requestDTO;
    private CompanyResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setId(1L);
        company.setCompanyName("Test Company");
        company.setDomain("test.com");

        requestDTO = new CompanyRequestDTO();
        requestDTO.setCompanyName("Test Company");
        requestDTO.setDomain("test.com");

        responseDTO = new CompanyResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCompanyName("Test Company");
        responseDTO.setDomain("test.com");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createCompany_shouldSaveAndReturnCompany_withoutRelations() {
        when(companyRepository.findByDomain("test.com")).thenReturn(Optional.empty());
        when(companyMapper.toEntity(requestDTO)).thenReturn(company);
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        CompanyResponseDTO result = companyService.createCompany(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getDomain()).isEqualTo("test.com");
        verify(companyRepository).save(company);
    }

    @Test
    void createCompany_shouldThrowException_whenDomainAlreadyExists() {
        when(companyRepository.findByDomain("test.com")).thenReturn(Optional.of(new Company()));

        assertThrows(DomainAlreadyExistsException.class, () -> companyService.createCompany(requestDTO));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void createCompany_shouldSetOwner_whenOwnerIdProvided() {
        requestDTO.setOwnerId(10L);
        Person owner = new Person();
        owner.setId(10L);

        when(companyRepository.findByDomain("test.com")).thenReturn(Optional.empty());
        when(companyMapper.toEntity(requestDTO)).thenReturn(company);
        when(personRepository.findById(10L)).thenReturn(Optional.of(owner));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        companyService.createCompany(requestDTO);

        assertThat(company.getOwner()).isEqualTo(owner);
        verify(personRepository).findById(10L);
    }

    @Test
    void createCompany_shouldThrowException_whenOwnerNotFound() {
        requestDTO.setOwnerId(10L);

        when(companyRepository.findByDomain("test.com")).thenReturn(Optional.empty());
        when(companyMapper.toEntity(requestDTO)).thenReturn(company);
        when(personRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> companyService.createCompany(requestDTO));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void createCompany_shouldSetUser_whenUserIdProvided() {
        requestDTO.setUserId(20L);
        User user = new User();
        user.setId(20L);

        when(companyRepository.findByDomain("test.com")).thenReturn(Optional.empty());
        when(companyMapper.toEntity(requestDTO)).thenReturn(company);
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        companyService.createCompany(requestDTO);

        assertThat(company.getUser()).isEqualTo(user);
    }

    @Test
    void createCompany_shouldThrowException_whenUserNotFound() {
        requestDTO.setUserId(20L);

        when(companyRepository.findByDomain("test.com")).thenReturn(Optional.empty());
        when(companyMapper.toEntity(requestDTO)).thenReturn(company);
        when(userRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> companyService.createCompany(requestDTO));
        verify(companyRepository, never()).save(any());
    }

    // =========================
    // GET ALL (Pageable)
    // =========================
    @Test
    void getAllCompanies_shouldReturnPagedCompanies() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Company> companyPage = new PageImpl<>(List.of(company));

        when(companyRepository.findAll(pageable)).thenReturn(companyPage);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        Page<CompanyResponseDTO> result = companyService.getAllCompanies(pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getCompanyName()).isEqualTo("Test Company");
        verify(companyRepository).findAll(pageable);
    }

    @Test
    void getAllCompanies_shouldReturnEmptyPage_whenNoCompanies() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Company> emptyPage = new PageImpl<>(List.of());

        when(companyRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<CompanyResponseDTO> result = companyService.getAllCompanies(pageable);

        assertThat(result.getContent()).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getCompanyById_shouldReturnCompany_whenExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        CompanyResponseDTO result = companyService.getCompanyById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(companyRepository).findById(1L);
    }

    @Test
    void getCompanyById_shouldThrowException_whenNotFound() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.getCompanyById(99L));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateCompany_shouldUpdateCompany_whenDomainUnchanged() {
        requestDTO.setCompanyName("Updated Name");
        requestDTO.setDomain("test.com"); // eyni domain

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        CompanyResponseDTO result = companyService.updateCompany(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(companyMapper).updateEntity(company, requestDTO);
        verify(companyRepository, never()).findByDomain(any());
        verify(companyRepository).save(company);
    }

    @Test
    void updateCompany_shouldThrowException_whenCompanyNotFound() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.updateCompany(99L, requestDTO));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void updateCompany_shouldThrowException_whenNewDomainAlreadyExists() {
        requestDTO.setDomain("newdomain.com");

        Company otherCompany = new Company();
        otherCompany.setId(2L);
        otherCompany.setDomain("newdomain.com");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.findByDomain("newdomain.com")).thenReturn(Optional.of(otherCompany));

        assertThrows(DomainAlreadyExistsException.class, () -> companyService.updateCompany(1L, requestDTO));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void updateCompany_shouldAllowUpdate_whenNewDomainIsFree() {
        requestDTO.setDomain("newdomain.com");

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.findByDomain("newdomain.com")).thenReturn(Optional.empty());
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        CompanyResponseDTO result = companyService.updateCompany(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(companyRepository).save(company);
    }

    @Test
    void updateCompany_shouldSetUser_whenDomainChangedAndUserIdProvided() {
        requestDTO.setDomain("newdomain.com");
        requestDTO.setUserId(20L);
        User user = new User();
        user.setId(20L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.findByDomain("newdomain.com")).thenReturn(Optional.empty());
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        companyService.updateCompany(1L, requestDTO);

        assertThat(company.getUser()).isEqualTo(user);
    }

    @Test
    void updateCompany_shouldThrowException_whenDomainChangedAndUserNotFound() {
        requestDTO.setDomain("newdomain.com");
        requestDTO.setUserId(20L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.findByDomain("newdomain.com")).thenReturn(Optional.empty());
        when(userRepository.findById(20L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> companyService.updateCompany(1L, requestDTO));
        verify(companyRepository, never()).save(any());
    }

    @Test
    void updateCompany_shouldNotCheckUser_whenDomainUnchangedEvenIfUserIdProvided() {
        requestDTO.setDomain("test.com"); // eyni domain
        requestDTO.setUserId(20L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        companyService.updateCompany(1L, requestDTO);

        // Domain dəyişmədiyi üçün userId yoxlanılmamalıdır (kod strukturuna görə)
        verify(userRepository, never()).findById(any());
        assertThat(company.getUser()).isNull();
    }

    @Test
    void updateCompany_shouldUpdateOwner_whenOwnerIdProvided() {
        requestDTO.setOwnerId(10L);
        Person owner = new Person();
        owner.setId(10L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(personRepository.findById(10L)).thenReturn(Optional.of(owner));
        when(companyRepository.save(company)).thenReturn(company);
        when(companyMapper.toResponseDTO(company)).thenReturn(responseDTO);

        companyService.updateCompany(1L, requestDTO);

        assertThat(company.getOwner()).isEqualTo(owner);
    }

    @Test
    void updateCompany_shouldThrowException_whenOwnerNotFound() {
        requestDTO.setOwnerId(10L);

        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
        when(personRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> companyService.updateCompany(1L, requestDTO));
        verify(companyRepository, never()).save(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteCompany_shouldDeleteCompany_whenExists() {
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

        companyService.deleteCompany(1L);

        verify(companyRepository).delete(company);
    }

    @Test
    void deleteCompany_shouldThrowException_whenNotFound() {
        when(companyRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CompanyNotFoundException.class, () -> companyService.deleteCompany(99L));
        verify(companyRepository, never()).delete(any());
    }

    // =========================
    // FILTERS
    // =========================
    @Test
    void getCompaniesByStatus_shouldReturnFilteredCompanies() {
        when(companyRepository.findByStatus(CompanyStatus.INTERESTED)).thenReturn(List.of(company));
        when(companyMapper.toResponseList(List.of(company))).thenReturn(List.of(responseDTO));

        List<CompanyResponseDTO> result = companyService.getCompaniesByStatus(CompanyStatus.INTERESTED);

        assertThat(result).hasSize(1);
        verify(companyRepository).findByStatus(CompanyStatus.INTERESTED);
    }

    @Test
    void getCompaniesByOwner_shouldReturnFilteredCompanies() {
        when(companyRepository.findByOwnerId(10L)).thenReturn(List.of(company));
        when(companyMapper.toResponseList(List.of(company))).thenReturn(List.of(responseDTO));

        List<CompanyResponseDTO> result = companyService.getCompaniesByOwner(10L);

        assertThat(result).hasSize(1);
        verify(companyRepository).findByOwnerId(10L);
    }

    @Test
    void searchCompanies_shouldReturnMatchingCompanies() {
        when(companyRepository.findByCompanyNameContainingIgnoreCase("test")).thenReturn(List.of(company));
        when(companyMapper.toResponseList(List.of(company))).thenReturn(List.of(responseDTO));

        List<CompanyResponseDTO> result = companyService.searchCompanies("test");

        assertThat(result).hasSize(1);
        verify(companyRepository).findByCompanyNameContainingIgnoreCase("test");
    }

    @Test
    void getCompaniesByUser_shouldReturnFilteredCompanies() {
        when(companyRepository.findByUserId(20L)).thenReturn(List.of(company));
        when(companyMapper.toResponseList(List.of(company))).thenReturn(List.of(responseDTO));

        List<CompanyResponseDTO> result = companyService.getCompaniesByUser(20L);

        assertThat(result).hasSize(1);
        verify(companyRepository).findByUserId(20L);
    }

    @Test
    void getCompaniesByUserAndStatus_shouldReturnFilteredCompanies() {
        when(companyRepository.findByUserIdAndStatus(20L, CompanyStatus.INTERESTED)).thenReturn(List.of(company));
        when(companyMapper.toResponseList(List.of(company))).thenReturn(List.of(responseDTO));

        List<CompanyResponseDTO> result = companyService.getCompaniesByUserAndStatus(20L, CompanyStatus.INTERESTED);

        assertThat(result).hasSize(1);
        verify(companyRepository).findByUserIdAndStatus(20L, CompanyStatus.INTERESTED);
    }
}
