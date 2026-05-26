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

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@Transactional
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
        // Domain mövcuddurmu yoxla
        if (organizationRepository.existsByDomain(requestDTO.getDomain())) {
            throw new DomainAlreadyExistsException("Bu domain artıq mövcuddur: " + requestDTO.getDomain());
        }

        Organization organization = organizationMapper.toEntity(requestDTO);

        // Manageri set et
        if (requestDTO.getManagerId() != null) {
            User manager = userRepository.findById(requestDTO.getManagerId())
                    .orElseThrow(() -> new UserNotFoundException("Manager tapılmadı!"));
            organization.setManager(manager);
        }

        return organizationMapper.toResponseDTO(organizationRepository.save(organization));
    }

    @Override
    public List<OrganizationResponseDTO> getAllOrganizations() {
        return organizationRepository.findAll()
                .stream()
                .map(organizationMapper::toResponseDTO)
                .toList();
    }

    @Override
    public OrganizationResponseDTO getOrganizationById(Long id) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization tapılmadı! ID: " + id));

        return organizationMapper.toResponseDTO(organization);
    }

    @Override
    public OrganizationResponseDTO updateOrganization(Long id, OrganizationRequestDTO requestDTO) {
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization tapılmadı! ID: " + id));

        // DOMAIN DUPLICATE CHECK
        Optional<Organization> existing =
                organizationRepository.findByDomain(requestDTO.getDomain());

        if (existing.isPresent() &&
                !existing.get().getId().equals(id)) {

            throw new DomainAlreadyExistsException(
                    "Bu domain artıq mövcuddur: " + requestDTO.getDomain()
            );
        }

        organizationMapper.updateEntity(organization, requestDTO);

        if (requestDTO.getManagerId() != null) {
            User manager = userRepository.findById(requestDTO.getManagerId())
                    .orElseThrow(() -> new UserNotFoundException("Manager tapılmadı!"));
            organization.setManager(manager);
        }

        return organizationMapper.toResponseDTO(organizationRepository.save(organization));
    }

    @Override
    public void deleteOrganization(Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization tapılmadı! ID: " + id));

        organizationRepository.delete(organization);
    }
}
