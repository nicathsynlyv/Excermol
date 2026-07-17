package com.example.Excermol.controller;

import com.example.Excermol.Service.PersonService;

import com.example.Excermol.entity.dtos.PersonRequestDTO;
import com.example.Excermol.entity.dtos.PersonResponseDTO;
import com.example.Excermol.enums.PersonStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
@Tag(name = "Person Controller", description = "Person CRUD əməliyyatları")

public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @Operation(summary = "Yeni person yarat")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Person uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping
    public ResponseEntity<PersonResponseDTO> createPerson(
            @Valid @RequestBody PersonRequestDTO requestDTO) {
        return ResponseEntity.status(201).body(personService.createPerson(requestDTO));
    }

    @Operation(summary = "Bütün person-ları gətir")
    @ApiResponse(responseCode = "200", description = "Siyahı uğurla qaytarıldı",
            content = @Content(schema = @Schema(implementation = PersonResponseDTO.class)))
    @GetMapping
    public ResponseEntity<List<PersonResponseDTO>> getAllPersons() {
        return ResponseEntity.ok(personService.getAllPersons());
    }

    @Operation(summary = "ID ilə person gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person tapıldı",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Person tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> getPersonById(@PathVariable Long id) {
        return ResponseEntity.ok(personService.getPersonById(id));
    }

    @Operation(summary = "Person yenilə")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Person uğurla yeniləndi",
                    content = @Content(schema = @Schema(implementation = PersonResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Person tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponseDTO> updatePerson(
            @PathVariable Long id,
            @Valid @RequestBody PersonRequestDTO requestDTO) {
        return ResponseEntity.ok(personService.updatePerson(id, requestDTO));
    }

    @Operation(summary = "Person sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Person uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Person tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }


    //user new changes
    @Operation(summary = "User-ə məxsus bütün persons")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Persons siyahısı uğurla qaytarıldı"),
            @ApiResponse(responseCode = "404", description = "User tapılmadı")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PersonResponseDTO>> getPersonsByUser(
            @PathVariable Long userId) {
        return ResponseEntity.ok(
                personService.getPersonsByUser(userId)
        );
    }

//user new changes

    @Operation(summary = "User-ə məxsus persons + status filter")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filterlənmiş persons siyahısı qaytarıldı"),
            @ApiResponse(responseCode = "400", description = "Status dəyəri yanlışdır"),
            @ApiResponse(responseCode = "404", description = "User tapılmadı"
            )
    })
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<PersonResponseDTO>> getPersonsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable PersonStatus status) {
        return ResponseEntity.ok(
                personService.getPersonsByUserAndStatus(userId, status)
        );
    }


}