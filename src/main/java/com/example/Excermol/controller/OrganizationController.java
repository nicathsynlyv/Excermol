package com.example.Excermol.controller;

import com.example.Excermol.Service.OrganizationService;
import com.example.Excermol.entity.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    // Yeni organization yaratmaq
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.createOrganization(organization));
    }

    // Bütün organization-ları gətir
    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationService.getAllOrganizations());
    }

    // ID ilə gətir
    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        return ResponseEntity.ok(organizationService.getOrganizationById(id));
    }

    // Yenilə
    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Long id,
                                                           @RequestBody Organization updatedOrg) {
        return ResponseEntity.ok(organizationService.updateOrganization(id, updatedOrg));
    }

    // Sil
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.noContent().build();
    }
}
