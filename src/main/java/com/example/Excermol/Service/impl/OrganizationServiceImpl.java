package com.example.Excermol.Service.impl;

import com.example.Excermol.Service.OrganizationService;
import com.example.Excermol.entity.Organization;
import com.example.Excermol.exception.OrganizationNotFoundException;
import com.example.Excermol.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    // BaseService metodları ilə uyğunlaşdırılmış
    @Override
    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    @Override
    public Optional<Organization> getById(Long id) {
        return organizationRepository.findById(id);
    }

    @Override
    public Organization save(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public void deleteById(Long id) {
        organizationRepository.deleteById(id);
    }

    // Organization-u update etmək
    @Override
    public Organization updateOrganization(Long id, Organization updatedOrg) {
        Organization org = organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization ID = " + id + " tapılmadı!"));

        org.setName(updatedOrg.getName());
        org.setDomain(updatedOrg.getDomain());
        org.setDescription(updatedOrg.getDescription());
        org.setManager(updatedOrg.getManager());
        org.setLastInteractedAt(updatedOrg.getLastInteractedAt());
        org.setListName(updatedOrg.getListName());
        org.setNumberOfDeals(updatedOrg.getNumberOfDeals());
        org.setEmployeesRange(updatedOrg.getEmployeesRange());

        return organizationRepository.save(org);
    }
}
