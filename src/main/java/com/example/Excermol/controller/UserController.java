package com.example.Excermol.controller;

import com.example.Excermol.entity.User;
import com.example.Excermol.Service.impl.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Operation(summary = "Yeni istifadəçi yaratmaq", description = "Verilən məlumatlara əsasən yeni istifadəçi əlavə edir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "İstifadəçi uğurla yaradıldı",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Yanlış request body", content = @Content)
    })
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userServiceImpl.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @Operation(summary = "ID üzrə istifadəçi tapmaq", description = "Verilən ID-yə görə istifadəçi qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "İstifadəçi tapıldı",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userServiceImpl.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Email üzrə istifadəçi tapmaq", description = "Email ünvanına görə istifadəçi qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "İstifadəçi tapıldı",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı", content = @Content)
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userServiceImpl.findByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Bütün istifadəçiləri səhifələmək", description = "Səhifələmə ilə bütün istifadəçiləri gətirir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Siyahı qaytarıldı",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userServiceImpl.findAll(pageable));
    }

    @Operation(summary = "İstifadəçi axtarışı", description = "Keyword əsasında istifadəçiləri axtarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Axtarış nəticəsi qaytarıldı",
                    content = @Content(schema = @Schema(implementation = Page.class)))
    })
    @GetMapping("/search")
    public ResponseEntity<Page<User>> searchUsers(@RequestParam String keyword, Pageable pageable) {
        return ResponseEntity.ok(userServiceImpl.searchUsers(keyword, pageable));
    }

    @Operation(summary = "İstifadəçini yeniləmək", description = "Verilən ID-yə əsasən istifadəçi məlumatlarını yeniləyir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "İstifadəçi uğurla yeniləndi",
                    content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User user = userServiceImpl.updateUser(id, updatedUser);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "İstifadəçini silmək", description = "Verilən ID-yə əsasən istifadəçini silir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Uğurla silindi", content = @Content),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServiceImpl.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Son login vaxtını yeniləmək", description = "Email əsasında istifadəçinin son login vaxtını yeniləyir")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Son login vaxtı uğurla yeniləndi", content = @Content),
            @ApiResponse(responseCode = "404", description = "İstifadəçi tapılmadı", content = @Content)
    })
    @PutMapping("/last-login")
    public ResponseEntity<Void> updateLastLogin(@RequestParam String email) {
        userServiceImpl.updateLastLogin(email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Aktiv istifadəçiləri gətirmək", description = "Aktiv statuslu istifadəçiləri siyahı şəklində qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Siyahı qaytarıldı",
                    content = @Content(schema = @Schema(implementation = User.class)))
    })
    @GetMapping("/active")
    public ResponseEntity<List<User>> getActiveUsers() {
        return ResponseEntity.ok(userServiceImpl.findActiveUsers());
    }

    @Operation(summary = "Aktiv istifadəçilərin sayını gətirmək", description = "Aktiv statuslu istifadəçilərin ümumi sayını qaytarır")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Say uğurla qaytarıldı",
                    content = @Content(schema = @Schema(implementation = Long.class)))
    })
    @GetMapping("/active/count")
    public ResponseEntity<Long> getActiveUserCount() {
        return ResponseEntity.ok(userServiceImpl.getActiveUserCount());
    }
}
