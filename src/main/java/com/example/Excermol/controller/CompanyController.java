//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.CompanyService;
//import com.example.Excermol.Service.impl.CompanyServiceImpl;
//import com.example.Excermol.entity.Company;
//import com.example.Excermol.enums.CompanyStatus;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/companies")
//public class CompanyController {
//
//    private final CompanyServiceImpl companyServiceImpl;
//
//    public CompanyController(CompanyServiceImpl companyServiceImpl) {
//        this.companyServiceImpl = companyServiceImpl;
//    }
//
//    @Operation(summary = "Get all companies", description = "Bütün şirkətlərin siyahısını gətirir")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
//    })
//    @GetMapping
//    public ResponseEntity<List<Company>> getAllCompanies() {
//        return ResponseEntity.ok(companyServiceImpl.getAll());
//    }
//
//    @Operation(summary = "Get company by ID", description = "ID-yə görə şirkət tapır")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Company found"),
//            @ApiResponse(responseCode = "404", description = "Company not found")
//    })
//    @GetMapping("/{id}")
//    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
//        return companyServiceImpl.getById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @Operation(summary = "Create or update company", description = "Yeni şirkət yaradır və ya mövcud olanı yeniləyir")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Company saved successfully")
//    })
//    @PostMapping
//    public ResponseEntity<Company> saveCompany(@RequestBody Company company) {
//        return ResponseEntity.ok(companyServiceImpl.save(company));
//    }
//
//    @Operation(summary = "Delete company by ID", description = "ID-yə görə şirkəti silir")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "204", description = "Company deleted successfully"),
//            @ApiResponse(responseCode = "404", description = "Company not found")
//    })
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
//        if (companyServiceImpl.getById(id).isPresent()) {
//            companyServiceImpl.deleteById(id);
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.notFound().build();
//    }
//
//    // 🔎 Əlavə query metodları
//
//    @Operation(summary = "Get company by name", description = "Şirkəti adı ilə axtarır")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Company found"),
//            @ApiResponse(responseCode = "404", description = "Company not found")
//    })
//    @GetMapping("/name/{companyName}")
//    public ResponseEntity<Company> getByCompanyName(@PathVariable String companyName) {
//        return companyServiceImpl.getByCompanyName(companyName)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @Operation(summary = "Get company by domain", description = "Şirkəti domain-ə görə tapır")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Company found"),
//            @ApiResponse(responseCode = "404", description = "Company not found")
//    })
//    @GetMapping("/domain/{domain}")
//    public ResponseEntity<Company> getByDomain(@PathVariable String domain) {
//        return companyServiceImpl.getByDomain(domain)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @Operation(summary = "Get companies by status", description = "Statusuna görə şirkətləri filterləyir")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Companies found")
//    })
//    @GetMapping("/status/{status}")
//    public ResponseEntity<List<Company>> getByStatus(@PathVariable CompanyStatus status) {
//        return ResponseEntity.ok(companyServiceImpl.getByStatus(status));
//    }
//}
