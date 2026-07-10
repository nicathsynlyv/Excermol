package com.example.Excermol.controller;

import com.example.Excermol.Service.PersonActivityService;
import com.example.Excermol.entity.dtos.PersonActivityResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people/{personId}/activities")
@Tag(name = "PersonActivity Controller", description = "Person Activity əməliyyatları")
public class PersonActivityController {

    private final PersonActivityService personActivityService;

    public PersonActivityController(PersonActivityService personActivityService) {
        this.personActivityService = personActivityService;
    }


    @Operation(summary = "Şəxsə aid bütün aktivlikləri gətir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aktivliklər uğurla qaytarıldı",
                    content = @Content(schema = @Schema(implementation = PersonActivityResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Person tapılmadı", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<PersonActivityResponseDTO>> getActivitiesByPersonId(
            @PathVariable Long personId) {
        return ResponseEntity.ok(personActivityService.getActivitiesByPersonId(personId));
    }

    @Operation(summary = "Activity sil")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "Activity tapılmadı", content = @Content)
    })
    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        personActivityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}