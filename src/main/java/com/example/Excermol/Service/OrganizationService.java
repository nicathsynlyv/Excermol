package com.example.Excermol.Service;

import com.example.Excermol.entity.Organization;

public interface OrganizationService extends BaseService<Organization,Long>{
    Organization updateOrganization(Long id, Organization updatedOrg); // 🔹 əlavə etdik

}
