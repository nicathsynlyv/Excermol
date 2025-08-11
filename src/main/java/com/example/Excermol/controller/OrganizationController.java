package com.example.Excermol.controller;

import com.example.Excermol.Service.OrganizationService;
import com.example.Excermol.entity.Organization;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public List<Organization> getAll() {
        return organizationService.getAll();
    }

    @GetMapping("/{id}")
    public Organization getById(@PathVariable Long id) {
        return organizationService.getById(id);
    }

    @PostMapping
    public Organization create(@RequestBody Organization organization) {
        return organizationService.create(organization);
    }

    @PutMapping("/{id}")
    public Organization update(@PathVariable Long id, @RequestBody Organization updated) {
        return organizationService.update(id, updated);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        organizationService.delete(id);
    }
}
