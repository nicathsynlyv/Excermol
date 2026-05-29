package com.example.Excermol.controller;

import com.example.Excermol.Service.CompanyService;
import com.example.Excermol.entity.dtos.CompanyRequestDTO;
import com.example.Excermol.entity.dtos.CompanyResponseDTO;
import com.example.Excermol.enums.CompanyStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/companies")
@Tag(name = "Company", description = "Şirkət əməliyyatları")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }


    @Operation(summary = "Şirkət yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Şirkət yaradıldı"),
            @ApiResponse(responseCode = "409", description = "Domain artıq mövcuddur"),
            @ApiResponse(responseCode = "400", description = "Validation xətası")
    })
    @PostMapping
    public ResponseEntity<CompanyResponseDTO> createCompany(
            @Valid @RequestBody CompanyRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(companyService.createCompany(requestDTO));
    }

    @Operation(summary = "Bütün şirkətləri gətir")
    @ApiResponse(responseCode = "200", description = "Siyahı gətirildi")
    @GetMapping
    public ResponseEntity<Page<CompanyResponseDTO>> getAllCompanies(
            Pageable pageable
    ) {
        return ResponseEntity.ok(companyService.getAllCompanies(pageable));
    }

    @Operation(summary = "ID ilə şirkət gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Şirkət tapıldı"),
            @ApiResponse(responseCode = "404", description = "Şirkət tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @Operation(summary = "Şirkəti yenilə")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Şirkət tapılmadı"),
            @ApiResponse(responseCode = "409", description = "Domain artıq mövcuddur")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponseDTO> updateCompany(
            @PathVariable Long id,
            @Valid @RequestBody CompanyRequestDTO requestDTO) {
        return ResponseEntity.ok(companyService.updateCompany(id, requestDTO));
    }

    @Operation(summary = "Şirkəti sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Silindi"),
            @ApiResponse(responseCode = "404", description = "Şirkət tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Statusa görə filter")
    @ApiResponse(responseCode = "200", description = "Siyahı gətirildi")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<CompanyResponseDTO>> getCompaniesByStatus(
            @PathVariable CompanyStatus status) {
        return ResponseEntity.ok(companyService.getCompaniesByStatus(status));
    }

    @Operation(summary = "Owner-a görə şirkətlər")
    @ApiResponse(responseCode = "200", description = "Siyahı gətirildi")
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CompanyResponseDTO>> getCompaniesByOwner(
            @PathVariable Long ownerId) {
        return ResponseEntity.ok(companyService.getCompaniesByOwner(ownerId));
    }

    @Operation(summary = "Şirkət adına görə axtarış")
    @ApiResponse(responseCode = "200", description = "Axtarış nəticələri")
    @GetMapping("/search")
    public ResponseEntity<List<CompanyResponseDTO>> searchCompanies(
            @RequestParam String name
    ) {
        return ResponseEntity.ok(
                companyService.searchCompanies(name)
        );
    }
}