//package com.example.Excermol.controller;
//
//import com.example.Excermol.Service.CompanyService;
//import com.example.Excermol.entity.Company;
//import com.example.Excermol.enums.CompanyStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/companies")
//public class CompanyController {
//    private final CompanyService companyService;
//
//    public CompanyController(CompanyService companyService) {
//        this.companyService = companyService;
//    }
//
//    @GetMapping
//    public List<Company> getAllCompanies() {
//        return companyService.getAllCompanies();
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
//        return companyService.getCompanyById(id)
//                .map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @PostMapping
//    public Company createCompany(@RequestBody Company company) {
//        return companyService.saveCompany(company);
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Company> updateCompany(@PathVariable Long id, @RequestBody Company companyDetails) {
//        return companyService.getCompanyById(id)
//                .map(existing -> {
//                    existing.setName(companyDetails.getName());
//                    existing.setEmailAddress(companyDetails.getEmailAddress());
//                    existing.setStatus(companyDetails.getStatus());
//                    existing.setCity(companyDetails.getCity());
//                    existing.setLeadValue(companyDetails.getLeadValue());
//                    existing.setConnection(companyDetails.getConnection());
//                    existing.setOwner(companyDetails.getOwner());
//                    existing.setLead(companyDetails.getLead());
//                    return ResponseEntity.ok(companyService.saveCompany(existing));
//                })
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
//        companyService.deleteCompany(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/status/{status}")
//    public List<Company> getByStatus(@PathVariable CompanyStatus status) {
//        return companyService.getCompaniesByStatus(status);
//    }
//
//    @GetMapping("/search")
//    public List<Company> searchByName(@RequestParam String name) {
//        return companyService.searchByName(name);
//    }
//}
