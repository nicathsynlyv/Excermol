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

    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
    }

    public Organization updateOrganization(Long id, Organization updatedOrg) {
        Organization org = getOrganizationById(id);

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

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }
}
