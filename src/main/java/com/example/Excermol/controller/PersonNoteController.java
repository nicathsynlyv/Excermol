package com.example.Excermol.controller;

import com.example.Excermol.Service.PersonNoteService;
import com.example.Excermol.entity.dtos.PersonNoteRequestDTO;
import com.example.Excermol.entity.dtos.PersonNoteResponseDTO;
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
@RequestMapping("/api/people/{personId}/notes")
@Tag(name = "PersonNote Controller", description = "Person Note əməliyyatları")
public class PersonNoteController {

    private final PersonNoteService personNoteService;

    public PersonNoteController(PersonNoteService personNoteService) {
        this.personNoteService = personNoteService;
    }

    @Operation(summary = "Şəxsə not əlavə et")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Not uğurla əlavə edildi",
                    content = @Content(schema = @Schema(implementation = PersonNoteResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PostMapping
    public ResponseEntity<PersonNoteResponseDTO> addNote(
            @PathVariable Long personId,
            @Valid @RequestBody PersonNoteRequestDTO requestDTO) {
        requestDTO.setPersonId(personId);
        return ResponseEntity.status(201).body(personNoteService.addNote(requestDTO));
    }

    @Operation(summary = "Şəxsə aid bütün notları gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notlar uğurla qaytarıldı",
                    content = @Content(schema = @Schema(implementation = PersonNoteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Person tapılmadı", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PersonNoteResponseDTO>> getNotesByPersonId(
            @PathVariable Long personId) {
        return ResponseEntity.ok(personNoteService.getNotesByPersonId(personId));
    }

    @Operation(summary = "Notu yenilə")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Not uğurla yeniləndi",
                    content = @Content(schema = @Schema(implementation = PersonNoteResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Not tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @PutMapping("/{noteId}")
    public ResponseEntity<PersonNoteResponseDTO> updateNote(
            @PathVariable Long personId,
            @PathVariable Long noteId,
            @Valid @RequestBody PersonNoteRequestDTO requestDTO) {
        requestDTO.setPersonId(personId);
        return ResponseEntity.ok(personNoteService.updateNote(personId, noteId, requestDTO));
    }

    @Operation(summary = "Notu sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Not uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not tapılmadı", content = @Content)
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @DeleteMapping("/{noteId}")
    public ResponseEntity<Void> deleteNote(
            @PathVariable Long personId, @PathVariable Long noteId) {
        personNoteService.deleteNote(personId, noteId);
        return ResponseEntity.noContent().build();
    }
}