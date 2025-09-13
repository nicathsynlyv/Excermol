package com.example.Excermol.controller;

import com.example.Excermol.entity.Activity;
import com.example.Excermol.entity.Person;
import com.example.Excermol.enums.ActivityAction;
import com.example.Excermol.Service.impl.ActivityServiceImpl;
import com.example.Excermol.service.PersonServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityServiceImpl activityServiceImpl;
    private final PersonServiceImpl personServiceImpl; // lazım olacaq, çünki person id-dən şəxs tapırıq

    // ---- CRUD ----

    @Operation(summary = "Get all activities", description = "Bütün activity siyahısını qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping
    public ResponseEntity<List<Activity>> getAll() {
        return ResponseEntity.ok(activityServiceImpl.getAll());
    }

    @Operation(summary = "Get activity by ID", description = "Verilən ID-yə görə activity qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity found"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Activity> getById(@PathVariable Long id) {
        return activityServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create or update activity", description = "Yeni activity əlavə edir və ya mövcud activity-ni update edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Activity saved successfully")
    })
    @PostMapping
    public ResponseEntity<Activity> save(@RequestBody Activity activity) {
        return ResponseEntity.ok(activityServiceImpl.save(activity));
    }

    @Operation(summary = "Delete activity by ID", description = "Verilən ID-yə görə activity silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Activity deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Activity not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        activityServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ---- Əlavə metodlar ----

    @Operation(summary = "Get activities by person ID", description = "Şəxs ID-yə görə activity-ləri qaytarır (ən yenisi birinci)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved activities by person")
    })
    @GetMapping("/person/{personId}")
    public ResponseEntity<List<Activity>> getByPerson(@PathVariable Long personId) {
        return personServiceImpl.getById(personId)
                .map(person -> ResponseEntity.ok(activityServiceImpl.getByPerson(person)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get activities by action type", description = "Action növünə görə activity-ləri qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved activities by action")
    })
    @GetMapping("/action/{action}")
    public ResponseEntity<List<Activity>> getByAction(@PathVariable ActivityAction action) {
        return ResponseEntity.ok(activityServiceImpl.getByAction(action));
    }

    @Operation(summary = "Get activities by person and action", description = "Şəxs və action növünə görə activity-ləri qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved activities by person and action")
    })
    @GetMapping("/person/{personId}/action/{action}")
    public ResponseEntity<List<Activity>> getByPersonAndAction(@PathVariable Long personId,
                                                               @PathVariable ActivityAction action) {
        return personServiceImpl.getById(personId)
                .map(person -> ResponseEntity.ok(activityServiceImpl.getByPersonAndAction(person, action)))
                .orElse(ResponseEntity.notFound().build());
    }
}
