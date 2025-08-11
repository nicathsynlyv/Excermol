package com.example.Excermol.Service;

import com.example.Excermol.entity.Organization;
import com.example.Excermol.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    public List<Organization> getAll() {
        return organizationRepository.findAll();
    }

    public Organization getById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    public Organization create(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Organization update(Long id, Organization updated) {
        Organization org = getById(id);
        org.setName(updated.getName());
        org.setDomains(updated.getDomains());
        org.setManager(updated.getManager());
        org.setLastInteractedAt(updated.getLastInteractedAt());
        org.setListName(updated.getListName());
        org.setNumberOfDeals(updated.getNumberOfDeals());
        org.setDescription(updated.getDescription());
        org.setEmployeesRange(updated.getEmployeesRange());
        return organizationRepository.save(org);
    }

    public void delete(Long id) {
        organizationRepository.deleteById(id);
    }
}
