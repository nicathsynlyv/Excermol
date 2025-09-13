package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.OrganizationServiceImpl;
import com.example.Excermol.entity.Organization;
import com.example.Excermol.exception.OrganizationNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    private final OrganizationServiceImpl organizationServiceImpl;

    public OrganizationController(OrganizationServiceImpl organizationServiceImpl) {
        this.organizationServiceImpl = organizationServiceImpl;
    }

    @Operation(summary = "Yeni organization yaratmaq", description = "Verilən məlumatlarla yeni organization əlavə edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Organization uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = Organization.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody Organization organization) {
        Organization savedOrg = organizationServiceImpl.save(organization);
        return ResponseEntity.status(201).body(savedOrg);
    }

    @Operation(summary = "Bütün organization-ları gətir", description = "Bazada mövcud olan bütün organization-ları qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı",
                    content = @Content(schema = @Schema(implementation = Organization.class)))
    })
    @GetMapping
    public ResponseEntity<List<Organization>> getAllOrganizations() {
        return ResponseEntity.ok(organizationServiceImpl.getAll());
    }

    @Operation(summary = "ID ilə organization gətir", description = "Verilmiş ID-yə əsasən organization qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization tapıldı",
                    content = @Content(schema = @Schema(implementation = Organization.class))),
            @ApiResponse(responseCode = "404", description = "Organization tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id) {
        return organizationServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Organization yeniləmək", description = "Verilmiş ID-yə əsasən organization məlumatlarını yeniləyir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Organization uğurla yeniləndi",
                    content = @Content(schema = @Schema(implementation = Organization.class))),
            @ApiResponse(responseCode = "404", description = "Organization tapılmadı", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(@PathVariable Long id,
                                                           @RequestBody Organization updatedOrg) {
        Organization org = organizationServiceImpl.updateOrganization(id, updatedOrg);
        return ResponseEntity.ok(org);
    }


    @Operation(summary = "Organization silmək", description = "Verilmiş ID-yə əsasən mövcud organization silinir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Organization uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Organization tapılmadı", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id) {
        Organization org = organizationServiceImpl.getById(id)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization ID = " + id + " tapılmadı!"));

        organizationServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}
