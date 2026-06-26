package com.example.Excermol.Service;

import com.example.Excermol.Service.impl.OrganizationServiceImpl;
import com.example.Excermol.entity.Organization;
import com.example.Excermol.entity.User;
import com.example.Excermol.entity.dtos.OrganizationRequestDTO;
import com.example.Excermol.entity.dtos.OrganizationResponseDTO;
import com.example.Excermol.exception.DomainAlreadyExistsException;
import com.example.Excermol.exception.OrganizationNotFoundException;
import com.example.Excermol.exception.UserNotFoundException;
import com.example.Excermol.mapper.OrganizationMapper;
import com.example.Excermol.repository.OrganizationRepository;
import com.example.Excermol.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("OrganizationServiceImpl Unit Tests")
class OrganizationServiceImplTest {

    @Mock
    private OrganizationRepository organizationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private OrganizationMapper organizationMapper;

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    private Organization organization;
    private OrganizationRequestDTO requestDTO;
    private OrganizationResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        organization = new Organization();
        organization.setId(1L);
        organization.setName("Test Org");
        organization.setDomain("test.com");

        requestDTO = new OrganizationRequestDTO();
        requestDTO.setName("Test Org");
        requestDTO.setDomain("test.com");

        responseDTO = new OrganizationResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setName("Test Org");
        responseDTO.setDomain("test.com");
    }

    // =========================
    // CREATE
    // =========================
    @Test
    void createOrganization_shouldSaveAndReturnOrganization_withoutManager() {
        when(organizationRepository.existsByDomain("test.com")).thenReturn(false);
        when(organizationMapper.toEntity(requestDTO)).thenReturn(organization);
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        OrganizationResponseDTO result = organizationService.createOrganization(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getDomain()).isEqualTo("test.com");
        verify(organizationRepository).save(organization);
    }

    @Test
    void createOrganization_shouldThrowException_whenDomainAlreadyExists() {
        when(organizationRepository.existsByDomain("test.com")).thenReturn(true);

        assertThrows(DomainAlreadyExistsException.class,
                () -> organizationService.createOrganization(requestDTO));

        verify(organizationRepository, never()).save(any());
    }

    @Test
    void createOrganization_shouldSetManager_whenManagerIdProvided() {
        requestDTO.setManagerId(5L);
        User manager = new User();
        manager.setId(5L);

        when(organizationRepository.existsByDomain("test.com")).thenReturn(false);
        when(organizationMapper.toEntity(requestDTO)).thenReturn(organization);
        when(userRepository.findById(5L)).thenReturn(Optional.of(manager));
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        organizationService.createOrganization(requestDTO);

        assertThat(organization.getManager()).isEqualTo(manager);
        verify(userRepository).findById(5L);
    }

    @Test
    void createOrganization_shouldThrowException_whenManagerNotFound() {
        requestDTO.setManagerId(5L);

        when(organizationRepository.existsByDomain("test.com")).thenReturn(false);
        when(organizationMapper.toEntity(requestDTO)).thenReturn(organization);
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> organizationService.createOrganization(requestDTO));

        verify(organizationRepository, never()).save(any());
    }

    // =========================
    // GET ALL
    // =========================
    @Test
    void getAllOrganizations_shouldReturnAllOrganizations() {
        when(organizationRepository.findAll()).thenReturn(List.of(organization));
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        List<OrganizationResponseDTO> result = organizationService.getAllOrganizations();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDomain()).isEqualTo("test.com");
        verify(organizationRepository).findAll();
    }

    @Test
    void getAllOrganizations_shouldReturnEmptyList_whenNoOrganizations() {
        when(organizationRepository.findAll()).thenReturn(List.of());

        List<OrganizationResponseDTO> result = organizationService.getAllOrganizations();

        assertThat(result).isEmpty();
    }

    // =========================
    // GET BY ID
    // =========================
    @Test
    void getOrganizationById_shouldReturnOrganization_whenExists() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        OrganizationResponseDTO result = organizationService.getOrganizationById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        verify(organizationRepository).findById(1L);
    }

    @Test
    void getOrganizationById_shouldThrowException_whenNotFound() {
        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrganizationNotFoundException.class,
                () -> organizationService.getOrganizationById(99L));
    }

    // =========================
    // UPDATE
    // =========================
    @Test
    void updateOrganization_shouldUpdateAndReturnOrganization() {
        requestDTO.setName("Updated Org");
        requestDTO.setDomain("test.com"); // eyni domain

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepository.findByDomain("test.com")).thenReturn(Optional.of(organization));
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        OrganizationResponseDTO result = organizationService.updateOrganization(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(organizationMapper).updateEntity(organization, requestDTO);
        verify(organizationRepository).save(organization);
    }

    @Test
    void updateOrganization_shouldThrowException_whenOrganizationNotFound() {
        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrganizationNotFoundException.class,
                () -> organizationService.updateOrganization(99L, requestDTO));

        verify(organizationRepository, never()).save(any());
    }

    @Test
    void updateOrganization_shouldThrowException_whenDomainBelongsToAnotherOrganization() {
        requestDTO.setDomain("other.com");

        Organization otherOrg = new Organization();
        otherOrg.setId(2L);
        otherOrg.setDomain("other.com");

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepository.findByDomain("other.com")).thenReturn(Optional.of(otherOrg));

        assertThrows(DomainAlreadyExistsException.class,
                () -> organizationService.updateOrganization(1L, requestDTO));

        verify(organizationRepository, never()).save(any());
    }

    @Test
    void updateOrganization_shouldAllowUpdate_whenDomainBelongsToSameOrganization() {
        requestDTO.setDomain("test.com");

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepository.findByDomain("test.com")).thenReturn(Optional.of(organization));
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        OrganizationResponseDTO result = organizationService.updateOrganization(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(organizationRepository).save(organization);
    }

    @Test
    void updateOrganization_shouldAllowUpdate_whenDomainNotFoundInRepository() {
        requestDTO.setDomain("newdomain.com");

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepository.findByDomain("newdomain.com")).thenReturn(Optional.empty());
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        OrganizationResponseDTO result = organizationService.updateOrganization(1L, requestDTO);

        assertThat(result).isNotNull();
        verify(organizationRepository).save(organization);
    }

    @Test
    void updateOrganization_shouldSetManager_whenManagerIdProvided() {
        requestDTO.setManagerId(5L);
        User manager = new User();
        manager.setId(5L);

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepository.findByDomain("test.com")).thenReturn(Optional.of(organization));
        when(userRepository.findById(5L)).thenReturn(Optional.of(manager));
        when(organizationRepository.save(organization)).thenReturn(organization);
        when(organizationMapper.toResponseDTO(organization)).thenReturn(responseDTO);

        organizationService.updateOrganization(1L, requestDTO);

        assertThat(organization.getManager()).isEqualTo(manager);
    }

    @Test
    void updateOrganization_shouldThrowException_whenManagerNotFound() {
        requestDTO.setManagerId(5L);

        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));
        when(organizationRepository.findByDomain("test.com")).thenReturn(Optional.of(organization));
        when(userRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> organizationService.updateOrganization(1L, requestDTO));

        verify(organizationRepository, never()).save(any());
    }

    // =========================
    // DELETE
    // =========================
    @Test
    void deleteOrganization_shouldDeleteOrganization_whenExists() {
        when(organizationRepository.findById(1L)).thenReturn(Optional.of(organization));

        organizationService.deleteOrganization(1L);

        verify(organizationRepository).delete(organization);
    }

    @Test
    void deleteOrganization_shouldThrowException_whenNotFound() {
        when(organizationRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(OrganizationNotFoundException.class,
                () -> organizationService.deleteOrganization(99L));

        verify(organizationRepository, never()).delete(any());
    }
}