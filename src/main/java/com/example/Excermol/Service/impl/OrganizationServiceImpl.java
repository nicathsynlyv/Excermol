package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.OrganizationService;
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
import jakarta.transaction.Transactional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final OrganizationMapper organizationMapper;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository, UserRepository userRepository, OrganizationMapper organizationMapper) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.organizationMapper = organizationMapper;
    }

    @Override
    public OrganizationResponseDTO createOrganization(OrganizationRequestDTO requestDTO) {
        log.info("Creating organization with domain: {}", requestDTO.getDomain());
        // Domain mövcuddurmu yoxla
        if (organizationRepository.existsByDomain(requestDTO.getDomain())) {
            log.warn("Domain already exists: {}", requestDTO.getDomain());
            throw new DomainAlreadyExistsException("Bu domain artıq mövcuddur: " + requestDTO.getDomain());
        }

        Organization organization = organizationMapper.toEntity(requestDTO);

        // Manageri set et
        if (requestDTO.getManagerId() != null) {
            log.info("Setting manager with id: {}", requestDTO.getManagerId());
            User manager = userRepository.findById(requestDTO.getManagerId())
                    .orElseThrow(() -> {
                        log.warn("Manager not found with id: {}", requestDTO.getManagerId());
                        return new UserNotFoundException("Manager tapılmadı!");
                    });
            organization.setManager(manager);
        }

        OrganizationResponseDTO response = organizationMapper.toResponseDTO(organizationRepository.save(organization));
        log.info("Organization created successfully with id: {}", response.getId());
        return response;
    }

    @Override
    public List<OrganizationResponseDTO> getAllOrganizations() {
        log.info("Fetching all organizations");
        List<OrganizationResponseDTO> organizations = organizationRepository.findAll()
                .stream()
                .map(organizationMapper::toResponseDTO)
                .toList();

        log.info("Retrieved {} organizations", organizations.size());
        return organizations;
        }

    @Override
    public OrganizationResponseDTO getOrganizationById(Long id) {
        log.info("Fetching organization with id: {}", id);

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Organization not found with id: {}", id);
                    return new OrganizationNotFoundException("Organization tapılmadı! ID: " + id);
                });

        log.info("Organization found with id: {}", id);
        return organizationMapper.toResponseDTO(organization);
    }

    @Override
    public OrganizationResponseDTO updateOrganization(Long id, OrganizationRequestDTO requestDTO) {
        log.info("Updating organization with id: {}", id);
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Organization not found for update. Id: {}", id);
                    return new OrganizationNotFoundException("Organization tapılmadı! ID: " + id);
                });


        // DOMAIN DUPLICATE CHECK
        Optional<Organization> existing =
                organizationRepository.findByDomain(requestDTO.getDomain());

        if (existing.isPresent() &&
                !existing.get().getId().equals(id)) {
            log.warn("Domain already exists: {}", requestDTO.getDomain());
            throw new DomainAlreadyExistsException("Bu domain artıq mövcuddur: " + requestDTO.getDomain()
            );
        }

        organizationMapper.updateEntity(organization, requestDTO);

        if (requestDTO.getManagerId() != null) {
            log.info("Updating manager with id: {}", requestDTO.getManagerId());
            User manager = userRepository.findById(requestDTO.getManagerId())
                    .orElseThrow(() -> {
                        log.warn("Manager not found with id: {}", requestDTO.getManagerId());
                        return new UserNotFoundException("Manager tapılmadı!");
                    });
            organization.setManager(manager);
        }

        OrganizationResponseDTO response = organizationMapper.toResponseDTO(organizationRepository.save(organization));
        log.info("Organization updated successfully. Id: {}", id);
        return response;
    }

    @Override
    public void deleteOrganization(Long id) {
        log.info("Deleting organization with id: {}", id);
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Organization not found for deletion. Id: {}", id);
                    return new OrganizationNotFoundException("Organization tapılmadı! ID: " + id);
                });
        organizationRepository.delete(organization);
        log.info("Organization deleted successfully. Id: {}", id);

    }
}
