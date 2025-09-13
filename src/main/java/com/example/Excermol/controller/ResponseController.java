package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.ResponseServiceImpl;
import com.example.Excermol.entity.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
@Tag(name = "Response API", description = "Form cavabları üçün əməliyyatlar")
public class ResponseController {

    private final ResponseServiceImpl responseServiceImpl;

    @Autowired
    public ResponseController(ResponseServiceImpl responseServiceImpl) {
        this.responseServiceImpl = responseServiceImpl;
    }

    @Operation(summary = "Bütün cavabları gətir", description = "Sistemdəki bütün response-ları qaytarır")
    @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    @GetMapping
    public ResponseEntity<List<Response>> getAllResponses() {
        return ResponseEntity.ok(responseServiceImpl.getAll());
    }

    @Operation(summary = "ID ilə cavabı tap", description = "Verilmiş ID-yə uyğun response-u qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cavab tapıldı"),
            @ApiResponse(responseCode = "404", description = "Cavab tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Response> getResponseById(@PathVariable Long id) {
        return responseServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni cavab əlavə et", description = "Yeni response yaradır və saxlayır")
    @ApiResponse(responseCode = "200", description = "Cavab uğurla yaradıldı")
    @PostMapping
    public ResponseEntity<Response> createResponse(@RequestBody Response response) {
        return ResponseEntity.ok(responseServiceImpl.save(response));
    }

    @Operation(summary = "Cavabı yenilə", description = "Mövcud response-un məlumatlarını dəyişir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cavab uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Cavab tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateResponse(@PathVariable Long id, @RequestBody Response response) {
        return responseServiceImpl.getById(id)
                .map(existingResponse -> {
                    response.setId(id);
                    return ResponseEntity.ok(responseServiceImpl.save(response));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Cavabı sil", description = "Verilmiş ID-yə uyğun response-u silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cavab uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Cavab tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable Long id) {
        if (responseServiceImpl.getById(id).isPresent()) {
            responseServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Form ID üzrə cavab sayını al", description = "Verilmiş form ID üçün response-ların sayını qaytarır")
    @ApiResponse(responseCode = "200", description = "Cavab sayı qaytarıldı")
    @GetMapping("/count/by-form/{formId}")
    public ResponseEntity<Long> countResponsesByFormId(@PathVariable Long formId) {
        return ResponseEntity.ok(responseServiceImpl.countByFormId(formId));
    }
}