package com.example.Excermol.controller;


import com.example.Excermol.Service.FormService;
import com.example.Excermol.Service.impl.FormServiceImpl;
import com.example.Excermol.entity.Form;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forms")
@Tag(name = "Form API", description = "Formlar üçün əməliyyatlar")
public class FormController {

    private final FormServiceImpl formServiceImpl;

    @Autowired
    public FormController(FormServiceImpl formServiceImpl) {
        this.formServiceImpl = formServiceImpl;
    }

    @Operation(summary = "Bütün formaları gətir", description = "Sistemdəki bütün formaların siyahısını qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    })
    @GetMapping
    public ResponseEntity<List<Form>> getAllForms() {
        return ResponseEntity.ok(formServiceImpl.getAll());
    }

    @Operation(summary = "ID ilə forma tap", description = "Verilmiş ID-yə uyğun formu qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma tapıldı"),
            @ApiResponse(responseCode = "404", description = "Forma tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Form> getFormById(@PathVariable Long id) {
        return formServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni forma əlavə et", description = "Yeni forma yaradır və saxlayır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma uğurla yaradıldı")
    })
    @PostMapping
    public ResponseEntity<Form> createForm(@RequestBody Form form) {
        Form savedForm = formServiceImpl.save(form);
        return ResponseEntity.ok(savedForm);
    }

    @Operation(summary = "Formanı yenilə", description = "Mövcud formanın məlumatlarını dəyişir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Forma uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Forma tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Form> updateForm(@PathVariable Long id, @RequestBody Form form) {
        return formServiceImpl.getById(id)
                .map(existingForm -> {
                    form.setId(id);
                    Form updatedForm = formServiceImpl.save(form);
                    return ResponseEntity.ok(updatedForm);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Formanı sil", description = "Verilmiş ID-yə uyğun formanı silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Forma uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Forma tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteForm(@PathVariable Long id) {
        if (formServiceImpl.getById(id).isPresent()) {
            formServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Formaları ada görə axtar", description = "Verilmiş ada uyğun formaların siyahısını qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Axtarış nəticələri qaytarıldı")
    })
    @GetMapping("/search")
    public ResponseEntity<List<Form>> searchForms(@RequestParam String name) {
        return ResponseEntity.ok(formServiceImpl.findByName(name));
    }

    @Operation(summary = "Formanın cavablarının sayını al", description = "Verilmiş form ID üçün cavabların ümumi sayını qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cavab sayı qaytarıldı")
    })
    @GetMapping("/{id}/responses/count")
    public ResponseEntity<Long> countResponses(@PathVariable Long id) {
        return ResponseEntity.ok(formServiceImpl.countResponses(id));
    }
}
