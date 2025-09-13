package com.example.Excermol.controller;

import com.example.Excermol.entity.Person;
import com.example.Excermol.enums.PersonStatus;
import com.example.Excermol.service.PersonServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {

    private final PersonServiceImpl personServiceImpl;

    // ---- CRUD ----

    @Operation(summary = "Get all persons", description = "Bütün person siyahısını qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<Person>> getAll() {
        return ResponseEntity.ok(personServiceImpl.getAll());
    }

    @Operation(summary = "Get person by ID", description = "Verilən ID-yə görə person qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person found"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Person> getById(@PathVariable Long id) {
        return personServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update person", description = "Yeni person əlavə edir və ya mövcud person-u update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person saved successfully")
    })
    @PostMapping
    public ResponseEntity<Person> save(@RequestBody Person person) {
        return ResponseEntity.ok(personServiceImpl.save(person));
    }

    @Operation(summary = "Delete person by ID", description = "Verilən ID-yə görə person silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Person not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        personServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Əlavə metodlar ----

    @Operation(summary = "Get persons by status", description = "Status-a görə person-ları qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved by status")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Person>> getByStatus(@PathVariable PersonStatus status) {
        return ResponseEntity.ok(personServiceImpl.getByStatus(status));
    }

    @Operation(summary = "Search persons by name", description = "Adına görə person-ları axtarır (case insensitive)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully searched by name")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Person>> searchByName(@RequestParam String name) {
        return ResponseEntity.ok(personServiceImpl.searchByName(name));
    }


    @Operation(summary = "Get all persons with pagination", description = "Pagination və sort ilə person siyahısını qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved paginated list")
    })
    @GetMapping("/paged")
    public ResponseEntity<Page<Person>> getAllWithPagination(Pageable pageable) {
        return ResponseEntity.ok(personServiceImpl.getAllWithPagination(pageable));
    }
}
