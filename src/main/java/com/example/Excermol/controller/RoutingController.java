package com.example.Excermol.controller;

import com.example.Excermol.Service.impl.RoutingServiceImpl;
import com.example.Excermol.entity.Routing;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routings")
@Tag(name = "Routing API", description = "Form routings üçün əməliyyatlar")
public class RoutingController {

    private final RoutingServiceImpl routingServiceImpl;

    @Autowired
    public RoutingController(RoutingServiceImpl routingServiceImpl) {
        this.routingServiceImpl = routingServiceImpl;
    }

    @Operation(summary = "Bütün routings-ləri gətir", description = "Sistemdəki bütün routing-ləri qaytarır")
    @ApiResponse(responseCode = "200", description = "Uğurlu əməliyyat")
    @GetMapping
    public ResponseEntity<List<Routing>> getAllRoutings() {
        return ResponseEntity.ok(routingServiceImpl.getAll());
    }

    @Operation(summary = "ID ilə routing tap", description = "Verilmiş ID-yə uyğun routing-i qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing tapıldı"),
            @ApiResponse(responseCode = "404", description = "Routing tapılmadı")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Routing> getRoutingById(@PathVariable Long id) {
        return routingServiceImpl.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Yeni routing əlavə et", description = "Yeni routing yaradır və saxlayır")
    @ApiResponse(responseCode = "200", description = "Routing uğurla yaradıldı")
    @PostMapping
    public ResponseEntity<Routing> createRouting(@RequestBody Routing routing) {
        return ResponseEntity.ok(routingServiceImpl.save(routing));
    }

    @Operation(summary = "Routing-i yenilə", description = "Mövcud routing-in məlumatlarını dəyişir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Routing uğurla yeniləndi"),
            @ApiResponse(responseCode = "404", description = "Routing tapılmadı")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Routing> updateRouting(@PathVariable Long id, @RequestBody Routing routing) {
        return routingServiceImpl.getById(id)
                .map(existingRouting -> {
                    routing.setId(id);
                    return ResponseEntity.ok(routingServiceImpl.save(routing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Routing-i sil", description = "Verilmiş ID-yə uyğun routing-i silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Routing uğurla silindi"),
            @ApiResponse(responseCode = "404", description = "Routing tapılmadı")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRouting(@PathVariable Long id) {
        if (routingServiceImpl.getById(id).isPresent()) {
            routingServiceImpl.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Routing title üzrə tap", description = "Verilmiş başlığa uyğun routing-i qaytarır")
    @ApiResponse(responseCode = "200", description = "Routing tapıldı")
    @GetMapping("/by-title")
    public ResponseEntity<Routing> findByRoutingTitle(@RequestParam String title) {
        return routingServiceImpl.findByRoutingTitle(title)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Contains üzrə routings-ləri tap", description = "Verilmiş contains mətnini özündə saxlayan routings-ləri qaytarır")
    @ApiResponse(responseCode = "200", description = "Routings-lər tapıldı")
    @GetMapping("/search")
    public ResponseEntity<List<Routing>> findByContains(@RequestParam String contains) {
        return ResponseEntity.ok(routingServiceImpl.findByContains(contains));
    }
}

